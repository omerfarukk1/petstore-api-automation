package petStore.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.response.Response;
import petStore.services.PetServices;

import java.util.ArrayList;
import java.util.List;

/**
 * Cucumber Hooks - Setup and Teardown
 * Runs before and after each scenario for initialization and cleanup
 */
public class Hooks {

    private static List<String> createdPetIds = new ArrayList<>();
    private PetServices petServices = new PetServices();

    /**
     * Runs BEFORE each scenario
     * Use this for setup/initialization
     */
    @Before
    public void setUp(Scenario scenario) {
        System.out.println("==========================================");
        System.out.println("▶️  Starting Scenario: " + scenario.getName());
        System.out.println("🏷️  Tags: " + scenario.getSourceTagNames());
        System.out.println("==========================================");

        // Clear the list of created pets for this scenario
        createdPetIds.clear();
    }

    /**
     * Runs AFTER each scenario
     * Use this for cleanup/teardown
     */
    @After
    public void tearDown(Scenario scenario) {
        System.out.println("==========================================");
        System.out.println("⏹️  Finished Scenario: " + scenario.getName());
        System.out.println("📊 Status: " + scenario.getStatus());
        System.out.println("==========================================");

        // Clean up test data - delete all pets created during this scenario
        cleanupTestPets();
    }

    /**
     * Runs AFTER failed scenarios only
     * Good for taking screenshots or extra logging
     */
    @After(order = 1)
    public void afterFailedScenario(Scenario scenario) {
        if (scenario.isFailed()) {
            System.err.println("❌ SCENARIO FAILED: " + scenario.getName());
            System.err.println("📍 URI: " + scenario.getUri());

            // You could add screenshot logic here for UI tests
            // Or additional API debugging info
        }
    }

    /**
     * Runs BEFORE scenarios with @smoke tag
     */
    @Before("@smoke")
    public void beforeSmokeTests() {
        System.out.println("🔥 Running SMOKE test - ensuring critical paths work");
    }

    /**
     * Runs AFTER scenarios with @deletePet tag
     */
    @After("@deletePet")
    public void afterDeleteTests() {
        System.out.println("🗑️  Delete test completed - verifying cleanup");
    }

    /**
     * Helper method to register a pet ID for cleanup
     * Call this from your step definitions when creating pets
     */
    public static void registerPetForCleanup(String petId) {
        if (petId != null && !createdPetIds.contains(petId)) {
            createdPetIds.add(petId);
            System.out.println("📝 Registered pet " + petId + " for cleanup");
        }
    }

    /**
     * Delete all pets that were created during the test
     * FIXED: Now handles 404 gracefully without throwing exceptions
     */
    private void cleanupTestPets() {
        if (createdPetIds.isEmpty()) {
            System.out.println("✅ No test pets to clean up");
            return;
        }

        System.out.println("🧹 Cleaning up " + createdPetIds.size() + " test pet(s)...");

        for (String petId : createdPetIds) {
            try {
                // Use the method without validation to avoid exceptions
                Response response = petServices.getPetByIdWithoutValidation(petId);
                int statusCode = response.getStatusCode();

                if (statusCode == 404) {
                    // Pet already deleted - this is OK
                    System.out.println("✅ Pet " + petId + " already deleted");
                    continue;
                }

                // Pet exists, try to delete it
                response = petServices.deleteByIdWithoutValidation(petId);
                statusCode = response.getStatusCode();

                if (statusCode == 200) {
                    System.out.println("✅ Deleted test pet: " + petId);
                } else if (statusCode == 404) {
                    System.out.println("✅ Pet " + petId + " was already deleted");
                } else {
                    System.out.println("⚠️  Unexpected status " + statusCode + " when deleting pet " + petId);
                }

            } catch (Exception e) {
                System.out.println("⚠️  Could not delete pet " + petId + ": " + e.getMessage());
                // Don't fail the test if cleanup fails - pet might already be deleted
            }
        }

        createdPetIds.clear();
        System.out.println("✅ Cleanup completed");
    }
}