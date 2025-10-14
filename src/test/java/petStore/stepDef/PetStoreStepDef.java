package petStore.stepDef;

import io.cucumber.java.en.*;
import io.qameta.allure.*;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import petStore.common.CommonRestCRUD;
import petStore.common.ConfigurationReader;
import petStore.services.PetServices;
import petStore.specs.PetReqSpec;
import petStore.hooks.Hooks;

import java.util.List;

import static io.restassured.RestAssured.given;

/**
 * Pet Store Step Definitions with Allure Reporting
 */
@Epic("Pet Store API")
@Feature("CRUD Operations")
public class PetStoreStepDef {

    private Response response;
    private PetServices petServices = new PetServices();
    private String petId;
    private String petName;

    // ============= GET REQUESTS =============

    @Step("Send GET request for pets with status: {status}")
    @Given("Send get request for status {string}")
    public void sendGetRequestForStatus(String status) {
        try {
            response = petServices.getPetStatus(status);
            Allure.addAttachment("Request Status", status);
            Allure.addAttachment("Response", response.getBody().asString());
            System.out.println("GET request sent for status: " + status);
        } catch (Exception e) {
            Allure.addAttachment("Error", e.getMessage());
            throw new RuntimeException("Failed to get pets by status: " + e.getMessage(), e);
        }
    }

    @Step("Send GET request for pet with ID: {id}")
    @When("Send get request for pet id {string}")
    public void sendGetRequestForPetId(String id) {
        try {
            response = petServices.getPetByIdWithoutValidation(id);
            Allure.addAttachment("Pet ID", id);
            Allure.addAttachment("Response", response.getBody().asString());
            System.out.println("GET request sent for pet id: " + id);
        } catch (Exception e) {
            Allure.addAttachment("Error", e.getMessage());
            throw new RuntimeException("Failed to get pet by id: " + e.getMessage(), e);
        }
    }

    // ============= POST REQUESTS =============

    @Step("Create new pet with ID: {id} and name: {name}")
    @Given("Create a new pet with id {string} and name {string}")
    public void createNewPet(String id, String name) {
        try {
            this.petId = id;
            this.petName = name;
            response = petServices.addNewPet(id, name);

            // Register pet for cleanup
            Hooks.registerPetForCleanup(id);

            Allure.parameter("Pet ID", id);
            Allure.parameter("Pet Name", name);
            Allure.addAttachment("Response Body", "application/json", response.getBody().asString(), ".json");

            System.out.println("POST request sent - Created pet: " + name + " (id: " + id + ")");
        } catch (Exception e) {
            Allure.addAttachment("Error", e.getMessage());
            throw new RuntimeException("Failed to create pet: " + e.getMessage(), e);
        }
    }

    // ============= PUT REQUESTS =============

    @Step("Update pet {id} with name: {newName} and tag: {tagName}")
    @Given("Update pet {string} with name {string} and tag {string}")
    public void updatePet(String id, String newName, String tagName) {
        try {
            response = petServices.updatePet(id, newName, tagName);

            Allure.parameter("Pet ID", id);
            Allure.parameter("New Name", newName);
            Allure.parameter("Tag", tagName);
            Allure.addAttachment("Response Body", "application/json", response.getBody().asString(), ".json");

            System.out.println("PUT request sent - Updated pet: " + newName + " with tag: " + tagName);
        } catch (Exception e) {
            Allure.addAttachment("Error", e.getMessage());
            throw new RuntimeException("Failed to update pet: " + e.getMessage(), e);
        }
    }

    // ============= DELETE REQUESTS =============

    @Step("Delete pet with ID: {id}")
    @Given("Delete pet with id {string}")
    public void deletePet(String id) {
        try {
            response = petServices.deleteByIdWithoutValidation(id);  // CHANGED THIS LINE
            System.out.println("DELETE request sent for pet id: " + id + " - Status: " + response.getStatusCode());
        } catch (Exception e) {
            System.out.println("DELETE request failed: " + e.getMessage());
        }
    }

    // ============= NEGATIVE TEST STEPS =============

    @Step("Create pet with invalid ID: {invalidId}")
    @Given("Create a pet with invalid id {string} and name {string}")
    public void createPetWithInvalidId(String invalidId, String name) {
        try {
            String body = "{ \"id\": \"" + invalidId + "\", \"name\": \"" + name + "\", \"photoUrls\": [\"string\"] }";

            RequestSpecification requestSpec = given()
                    .baseUri(ConfigurationReader.get("baseUri"))
                    .basePath(ConfigurationReader.get("addNewPetEndPoint"))
                    .header("Content-Type", "application/json")
                    .body(body);

            response = CommonRestCRUD.post(requestSpec);

            Allure.parameter("Invalid ID", invalidId);
            Allure.addAttachment("Request Body", "application/json", body, ".json");
            Allure.addAttachment("Response", response.getBody().asString());

            System.out.println("POST request sent with invalid ID type: " + invalidId);
        } catch (Exception e) {
            Allure.addAttachment("Expected Error", e.getMessage());
            System.out.println("POST request failed as expected: " + e.getMessage());
        }
    }

    @Step("Create pet with missing photoUrls field")
    @Given("Create a pet with missing photoUrls field")
    public void createPetWithMissingField() {
        try {
            RequestSpecification requestSpec = PetReqSpec.addInvalidPetReqSpec("101");
            response = CommonRestCRUD.post(requestSpec);

            Allure.addAttachment("Response", response.getBody().asString());
            System.out.println("POST request sent with missing required field (photoUrls)");
        } catch (Exception e) {
            Allure.addAttachment("Expected Error", e.getMessage());
            System.out.println("POST request failed as expected: " + e.getMessage());
        }
    }

    @Step("Send GET request for non-existent pet ID: {id}")
    @Given("Send get request for non-existent pet id {string}")
    public void sendGetRequestForNonExistentPet(String id) {
        try {
            response = petServices.getPetByIdWithoutValidation(id);

            Allure.parameter("Non-existent Pet ID", id);
            Allure.addAttachment("Response", response.getBody().asString());

            System.out.println("GET request sent for non-existent pet id: " + id);
        } catch (Exception e) {
            Allure.addAttachment("Expected Error", e.getMessage());
            System.out.println("GET request failed as expected: " + e.getMessage());
        }
    }

    // ============= RESPONSE ASSERTIONS =============

    @Step("Verify status code is {expectedStatus}")
    @Then("Verify status code is {int}")
    public void verifyStatusCode(int expectedStatus) {
        int actualStatus = response.getStatusCode();

        Allure.parameter("Expected Status", expectedStatus);
        Allure.parameter("Actual Status", actualStatus);

        assert actualStatus == expectedStatus :
                "Expected status code " + expectedStatus + " but got " + actualStatus;

        System.out.println("✓ Status code verified: " + actualStatus);
    }

    @Step("Verify pet name is {expectedName}")
    @Then("Verify pet name in response is {string}")
    public void verifyPetNameInResponse(String expectedName) {
        try {
            String actualName = response.jsonPath().getString("name");

            Allure.parameter("Expected Name", expectedName);
            Allure.parameter("Actual Name", actualName);

            assert actualName != null && actualName.equals(expectedName) :
                    "Expected pet name '" + expectedName + "' but got '" + actualName + "'";

            System.out.println("✓ Pet name verified: " + actualName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet name: " + e.getMessage(), e);
        }
    }

    @Step("Verify pet ID is {expectedId}")
    @Then("Verify pet id in response is {string}")
    public void verifyPetIdInResponse(String expectedId) {
        try {
            Long actualId = response.jsonPath().getLong("id");

            Allure.parameter("Expected ID", expectedId);
            Allure.parameter("Actual ID", actualId);

            assert actualId != null && actualId.toString().equals(expectedId) :
                    "Expected pet id '" + expectedId + "' but got '" + actualId + "'";

            System.out.println("✓ Pet id verified: " + actualId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet id: " + e.getMessage(), e);
        }
    }

    @Step("Verify pet name is {expectedName}")
    @Then("Verify pet name is {string}")
    public void verifyPetName(String expectedName) {
        try {
            String actualName = response.jsonPath().getString("name");

            Allure.parameter("Expected Name", expectedName);
            Allure.parameter("Actual Name", actualName);

            assert actualName != null && actualName.equals(expectedName) :
                    "Expected pet name '" + expectedName + "' but got '" + actualName + "'";

            System.out.println("✓ Pet name verified: " + actualName);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet name: " + e.getMessage(), e);
        }
    }

    @Step("Verify pet tag is {expectedTag}")
    @Then("Verify pet tag in response is {string}")
    public void verifyPetTagInResponse(String expectedTag) {
        try {
            List<Object> tags = response.jsonPath().getList("tags");

            assert tags != null && !tags.isEmpty() :
                    "Expected tags in response but got none";

            boolean tagFound = tags.stream()
                    .anyMatch(tag -> tag.toString().contains(expectedTag));

            Allure.parameter("Expected Tag", expectedTag);
            Allure.parameter("Actual Tags", tags.toString());

            assert tagFound :
                    "Expected tag '" + expectedTag + "' not found in tags: " + tags;

            System.out.println("✓ Pet tags verified: " + tags);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet tag: " + e.getMessage(), e);
        }
    }

    @Step("Verify pet tag is {expectedTag}")
    @Then("Verify pet tag is {string}")
    public void verifyPetTag(String expectedTag) {
        try {
            List<Object> tags = response.jsonPath().getList("tags");

            assert tags != null && !tags.isEmpty() :
                    "Expected tags in response but got none";

            boolean tagFound = tags.stream()
                    .anyMatch(tag -> tag.toString().contains(expectedTag));

            Allure.parameter("Expected Tag", expectedTag);
            Allure.parameter("Actual Tags", tags.toString());

            assert tagFound :
                    "Expected tag '" + expectedTag + "' not found in tags: " + tags;

            System.out.println("✓ Pet tags verified: " + tags);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet tag: " + e.getMessage(), e);
        }
    }

    @Step("Verify response contains pets")
    @Then("Verify response contains pets")
    public void verifyResponseContainsPets() {
        try {
            List<Object> pets = response.jsonPath().getList("$");

            assert pets != null && !pets.isEmpty() :
                    "Expected pets in response but list is empty";

            Allure.parameter("Number of Pets", pets.size());
            System.out.println("✓ Response contains " + pets.size() + " pets");
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pets in response: " + e.getMessage(), e);
        }
    }

    @Step("Verify error message contains: {expectedMessage}")
    @Then("Verify error message contains {string}")
    public void verifyErrorMessage(String expectedMessage) {
        try {
            String responseBody = response.getBody().asString();
            String actualMessage = null;

            try {
                actualMessage = response.jsonPath().getString("message");
            } catch (Exception e1) {
                try {
                    actualMessage = response.jsonPath().getString("error");
                } catch (Exception e2) {
                    try {
                        actualMessage = response.jsonPath().getString("type");
                    } catch (Exception e3) {
                        actualMessage = responseBody;
                    }
                }
            }

            Allure.parameter("Expected Message", expectedMessage);
            Allure.parameter("Actual Message", actualMessage);

            assert actualMessage != null && actualMessage.toLowerCase().contains(expectedMessage.toLowerCase()) :
                    "Expected error message containing '" + expectedMessage + "' but got '" + actualMessage + "'";

            System.out.println("✓ Error message verified: " + actualMessage);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify error message: " + e.getMessage(), e);
        }
    }

    @Step("Verify pet details are correct")
    @Then("Verify pet details are correct")
    public void verifyPetDetails() {
        try {
            String status = response.jsonPath().getString("status");
            List<String> photoUrls = response.jsonPath().getList("photoUrls");

            assert status != null : "Status should not be null";
            assert photoUrls != null : "Photo URLs should not be null";

            Allure.parameter("Pet Status", status);
            Allure.parameter("Photo URLs", photoUrls.toString());

            System.out.println("✓ Pet details verified - Status: " + status);
            System.out.println("✓ Photo URLs: " + photoUrls);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify pet details: " + e.getMessage(), e);
        }
    }

    @Step("Verify response time is less than {maxTime}ms")
    @Then("Verify response time is less than {int} milliseconds")
    public void verifyResponseTime(int maxTime) {
        long responseTime = response.getTime();

        Allure.parameter("Max Time (ms)", maxTime);
        Allure.parameter("Actual Time (ms)", responseTime);

        assert responseTime < maxTime :
                "Response time " + responseTime + "ms exceeded max " + maxTime + "ms";

        System.out.println("✓ Response time verified: " + responseTime + "ms (max: " + maxTime + "ms)");
    }

    @Step("Verify response status is {expectedStatus}")
    @Then("Verify response status is {string}")
    public void verifyResponseStatus(String expectedStatus) {
        try {
            String actualStatus = response.jsonPath().getString("status");

            Allure.parameter("Expected Status", expectedStatus);
            Allure.parameter("Actual Status", actualStatus);

            assert actualStatus != null && actualStatus.equals(expectedStatus) :
                    "Expected status '" + expectedStatus + "' but got '" + actualStatus + "'";

            System.out.println("✓ Pet status verified: " + actualStatus);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify status: " + e.getMessage(), e);
        }
    }
}