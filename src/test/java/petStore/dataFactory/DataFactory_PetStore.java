package petStore.dataFactory;

/**
 * Data Factory for PetStore API
 * Generates JSON request bodies for pet operations
 */
public class DataFactory_PetStore {

    // Default values
    public static final String DEFAULT_CATEGORY = "Dogs";
    public static final String DEFAULT_STATUS = "available";
    public static final String DEFAULT_PHOTO_URL = "https://example.com/photo.jpg";

    /**
     * Create JSON body for adding a new pet
     * @param id Pet ID
     * @param name Pet name
     * @return JSON string
     */
    public static String addNewPetBody(String id, String name) {
        return addNewPetBody(id, name, DEFAULT_STATUS);
    }

    /**
     * Create JSON body for adding a new pet with custom status
     * @param id Pet ID
     * @param name Pet name
     * @param status Pet status (available, pending, sold)
     * @return JSON string
     */
    public static String addNewPetBody(String id, String name, String status) {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"" + DEFAULT_CATEGORY + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + DEFAULT_PHOTO_URL + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"friendly\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"" + status + "\"\n" +
                "}";
    }

    /**
     * Create JSON body for adding a new pet with complete details
     * @param id Pet ID
     * @param name Pet name
     * @param categoryName Category name
     * @param status Pet status
     * @param tagName Tag name
     * @return JSON string
     */
    public static String addNewPetBodyComplete(String id, String name, String categoryName, String status, String tagName) {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"" + categoryName + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + DEFAULT_PHOTO_URL + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"" + tagName + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"" + status + "\"\n" +
                "}";
    }

    /**
     * Create JSON body for updating pet name and tag
     * @param id Pet ID
     * @param updateName New pet name
     * @param tagName New tag name
     * @return JSON string
     */
    public static String updatePetBody(String id, String updateName, String tagName) {
        return updatePetBody(id, updateName, tagName, DEFAULT_STATUS);
    }

    /**
     * Create JSON body for updating pet with custom status
     * @param id Pet ID
     * @param updateName New pet name
     * @param tagName New tag name
     * @param status Pet status
     * @return JSON string
     */
    public static String updatePetBody(String id, String updateName, String tagName, String status) {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"" + DEFAULT_CATEGORY + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + updateName + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + DEFAULT_PHOTO_URL + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                "    {\n" +
                "      \"id\": 1,\n" +
                "      \"name\": \"" + tagName + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"status\": \"" + status + "\"\n" +
                "}";
    }

    /**
     * Create JSON body with only required fields (minimal pet)
     * @param id Pet ID
     * @param name Pet name
     * @return JSON string
     */
    public static String minimalPetBody(String id, String name) {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"photoUrls\": [\"string\"]\n" +
                "}";
    }

    /**
     * Create invalid JSON body for negative testing (missing required field)
     * @param id Pet ID
     * @return JSON string without required photoUrls field
     */
    public static String invalidPetBodyMissingPhotoUrls(String id) {
        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"name\": \"TestPet\"\n" +
                "}";
    }

    /**
     * Create invalid JSON body with wrong data type
     * @return JSON string with invalid ID type
     */
    public static String invalidPetBodyWrongType() {
        return "{\n" +
                "  \"id\": \"not-a-number\",\n" +
                "  \"name\": \"TestPet\",\n" +
                "  \"photoUrls\": [\"string\"]\n" +
                "}";
    }

    /**
     * Create JSON body for pet with multiple tags
     * @param id Pet ID
     * @param name Pet name
     * @param tagNames Array of tag names
     * @return JSON string
     */
    public static String petBodyWithMultipleTags(String id, String name, String[] tagNames) {
        StringBuilder tags = new StringBuilder();

        for (int i = 0; i < tagNames.length; i++) {
            tags.append("    {\n")
                    .append("      \"id\": ").append(i + 1).append(",\n")
                    .append("      \"name\": \"").append(tagNames[i]).append("\"\n")
                    .append("    }");

            if (i < tagNames.length - 1) {
                tags.append(",\n");
            }
        }

        return "{\n" +
                "  \"id\": " + id + ",\n" +
                "  \"category\": {\n" +
                "    \"id\": 1,\n" +
                "    \"name\": \"" + DEFAULT_CATEGORY + "\"\n" +
                "  },\n" +
                "  \"name\": \"" + name + "\",\n" +
                "  \"photoUrls\": [\n" +
                "    \"" + DEFAULT_PHOTO_URL + "\"\n" +
                "  ],\n" +
                "  \"tags\": [\n" +
                tags.toString() + "\n" +
                "  ],\n" +
                "  \"status\": \"" + DEFAULT_STATUS + "\"\n" +
                "}";
    }

    /**
     * Create JSON body for pet with specific status
     * Useful for testing status filtering
     * @param id Pet ID
     * @param name Pet name
     * @param status Status (available, pending, sold)
     * @return JSON string
     */
    public static String petBodyWithStatus(String id, String name, String status) {
        return addNewPetBody(id, name, status);
    }

    /**
     * Generate random pet name for testing
     * @return Random pet name
     */
    public static String generateRandomPetName() {
        String[] petNames = {"Max", "Bella", "Charlie", "Luna", "Cooper", "Daisy", "Rocky", "Molly"};
        int randomIndex = (int) (Math.random() * petNames.length);
        return petNames[randomIndex] + "_" + System.currentTimeMillis();
    }

    /**
     * Generate random pet ID for testing
     * @return Random pet ID as string
     */
    public static String generateRandomPetId() {
        return String.valueOf(System.currentTimeMillis());
    }

    /**
     * Print sample JSON bodies for debugging
     */
    public static void printSampleBodies() {
        System.out.println("========== SAMPLE JSON BODIES ==========");
        System.out.println("\n1. Add New Pet:");
        System.out.println(addNewPetBody("101", "Zeus"));

        System.out.println("\n2. Update Pet:");
        System.out.println(updatePetBody("101", "Leo", "owner"));

        System.out.println("\n3. Minimal Pet:");
        System.out.println(minimalPetBody("102", "Toby"));

        System.out.println("\n4. Pet with Multiple Tags:");
        System.out.println(petBodyWithMultipleTags("103", "Max", new String[]{"friendly", "trained", "vaccinated"}));

        System.out.println("========================================");
    }
}