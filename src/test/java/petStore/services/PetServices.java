package petStore.services;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import petStore.common.ApiGenericFunctions;
import petStore.common.CommonRestCRUD;
import petStore.specs.PetReqSpec;

import static io.restassured.RestAssured.given;

/**
 * Pet Services - Handles all pet-related API operations
 * Extends CommonRestCRUD to inherit REST methods
 */
public class PetServices extends CommonRestCRUD {

    /**
     * Get pets by status
     * @param status Pet status (available, pending, sold)
     * @return Response object
     */
    public Response getPetStatus(String status) {
        System.out.println("🔍 Fetching pets with status: " + status);

        RequestSpecification requestSpec = PetReqSpec.getPetStatusReqSpec(status);
        Response response = get(requestSpec, 200);

        System.out.println("✓ Retrieved " + response.jsonPath().getList("$").size() + " pets");
        return response;
    }

    /**
     * Add a new pet to the store
     * @param id Pet ID
     * @param name Pet name
     * @return Response object
     */
    public Response addNewPet(String id, String name) {
        System.out.println("➕ Adding new pet: " + name + " (ID: " + id + ")");

        RequestSpecification requestSpec = PetReqSpec.addNewPetReqSpec(id, name);
        Response response = post(requestSpec, 200);

        System.out.println("✓ Pet created successfully");
        return response;
    }

    /**
     * Get pet by ID
     * @param id Pet ID
     * @return Response object
     */
    public Response getPetById(String id) {
        System.out.println("🔍 Fetching pet with ID: " + id);

        RequestSpecification requestSpec = PetReqSpec.getPetIdReqSpec(id);
        Response response = get(requestSpec, 200);

        System.out.println("✓ Pet retrieved: " + response.jsonPath().getString("name"));
        return response;
    }

    /**
     * Get pet by ID without status validation (for negative tests)
     * @param id Pet ID (can be invalid)
     * @return Response object
     */
    public Response getPetByIdWithoutValidation(String id) {
        System.out.println("🔍 Attempting to fetch pet with ID: " + id);

        RequestSpecification requestSpec = PetReqSpec.getPetIdReqSpec(id);
        Response response = get(requestSpec); // No status code validation

        System.out.println("Response status: " + response.getStatusCode());
        return response;
    }

    /**
     * Get pet by invalid ID (expects 404)
     * @param id Invalid pet ID
     * @return Response object
     */
    public Response getPetByIdInvalid(String id) {
        System.out.println("🔍 Fetching pet with invalid ID: " + id + " (expecting 404)");

        RequestSpecification requestSpec = PetReqSpec.getPetIdReqSpec(id);
        Response response = get(requestSpec, 404);

        System.out.println("✓ Received expected 404 response");
        return response;
    }

    /**
     * Update existing pet
     * @param id Pet ID
     * @param updateName New pet name
     * @param tagName Tag name
     * @return Response object
     */
    public Response updatePet(String id, String updateName, String tagName) {
        System.out.println("✏️ Updating pet ID " + id + " with name: " + updateName + ", tag: " + tagName);

        RequestSpecification requestSpec = PetReqSpec.updatePetReqSpec(id, updateName, tagName);
        Response response = put(requestSpec, 200);

        // Small wait to ensure data propagation (API may need time)
        System.out.println("⏳ Waiting 2 seconds for data propagation...");
        ApiGenericFunctions.waitForSec(2);

        System.out.println("✓ Pet updated successfully");
        return response;
    }

    /**
     * Delete pet by ID
     * @param id Pet ID
     * @return Response object
     */
    public Response deleteById(String id) {
        System.out.println("🗑️ Deleting pet with ID: " + id);

        RequestSpecification requestSpec = PetReqSpec.deletePetReqSpec(id);
        Response response = delete(requestSpec, 200);

        // Small wait to ensure deletion is processed
        System.out.println("⏳ Waiting 2 seconds for deletion to complete...");
        ApiGenericFunctions.waitForSec(2);

        System.out.println("✓ Pet deleted successfully");
        return response;
    }

    /**
     * Delete pet by ID without status code validation
     * Used for cleanup where we don't want to fail if pet is already deleted
     * @param id Pet ID
     * @return Response object
     */
    public Response deleteByIdWithoutValidation(String id) {
        System.out.println("🗑️ Attempting to delete pet: " + id + " (no validation)");

        try {
            RequestSpecification requestSpec = PetReqSpec.deletePetReqSpec(id);

            Response response = given()
                    .spec(requestSpec)
                    .when()
                    .delete()
                    .then()
                    .extract()
                    .response();

            System.out.println("Delete response status: " + response.getStatusCode());
            return response;

        } catch (Exception e) {
            System.out.println("⚠️ Error deleting pet without validation: " + e.getMessage());
            throw e;
        }
    }

    /**
     * Check if pet exists by ID
     * @param id Pet ID
     * @return true if pet exists (200), false if not found (404)
     */
    public boolean petExists(String id) {
        System.out.println("🔍 Checking if pet exists: " + id);

        RequestSpecification requestSpec = PetReqSpec.getPetIdReqSpec(id);
        Response response = get(requestSpec);

        boolean exists = response.getStatusCode() == 200;
        System.out.println(exists ? "✓ Pet exists" : "✗ Pet not found");
        return exists;
    }

    /**
     * Verify pet was created successfully
     * @param id Pet ID to verify
     * @param expectedName Expected pet name
     * @return true if pet exists with correct name
     */
    public boolean verifyPetCreation(String id, String expectedName) {
        System.out.println("✓ Verifying pet creation: ID=" + id + ", Name=" + expectedName);

        try {
            Response response = getPetById(id);
            String actualName = response.jsonPath().getString("name");

            boolean isValid = actualName != null && actualName.equals(expectedName);

            if (isValid) {
                System.out.println("✓ Pet verification successful");
            } else {
                System.out.println("✗ Pet verification failed: Expected name '" + expectedName + "' but got '" + actualName + "'");
            }

            return isValid;
        } catch (Exception e) {
            System.err.println("✗ Pet verification failed: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verify pet was deleted successfully
     * @param id Pet ID to verify
     * @return true if pet returns 404
     */
    public boolean verifyPetDeletion(String id) {
        System.out.println("✓ Verifying pet deletion: ID=" + id);

        try {
            Response response = getPetByIdWithoutValidation(id);
            boolean isDeleted = response.getStatusCode() == 404;

            if (isDeleted) {
                System.out.println("✓ Pet deletion verified (404 received)");
            } else {
                System.out.println("✗ Pet still exists (status: " + response.getStatusCode() + ")");
            }

            return isDeleted;
        } catch (Exception e) {
            System.err.println("✗ Pet deletion verification failed: " + e.getMessage());
            return false;
        }
    }
}