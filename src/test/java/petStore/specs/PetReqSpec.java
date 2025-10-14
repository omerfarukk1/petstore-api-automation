package petStore.specs;

import java.util.HashMap;
import io.restassured.specification.RequestSpecification;
import petStore.common.ConfigurationReader;
import petStore.dataFactory.DataFactory_PetStore;


import java.util.Map;

/**
 * Pet Request Specifications
 * Builds RequestSpecification objects for pet-related API calls
 * Extends CommonRequestSpec for shared functionality
 */
public class PetReqSpec extends CommonRequestSpec {

    /**
     * Build request spec for getting pets by status
     * @param status Pet status (available, pending, sold)
     * @return RequestSpecification
     */
    public static RequestSpecification getPetStatusReqSpec(String status) {
        String endpoint = ConfigurationReader.get("getStatusEndPoint");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("status", status);

        return petStoreRequestSpec(endpoint, null, queryParams, null);
    }

    /**
     * Build request spec for adding a new pet
     * @param id Pet ID
     * @param name Pet name
     * @return RequestSpecification
     */
    public static RequestSpecification addNewPetReqSpec(String id, String name) {
        String endpoint = ConfigurationReader.get("addNewPetEndPoint");
        String body = DataFactory_PetStore.addNewPetBody(id, name);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for adding a new pet with custom status
     * @param id Pet ID
     * @param name Pet name
     * @param status Pet status
     * @return RequestSpecification
     */
    public static RequestSpecification addNewPetReqSpec(String id, String name, String status) {
        String endpoint = ConfigurationReader.get("addNewPetEndPoint");
        String body = DataFactory_PetStore.addNewPetBody(id, name, status);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for getting pet by ID
     * @param id Pet ID
     * @return RequestSpecification
     */
    public static RequestSpecification getPetIdReqSpec(String id) {
        String endpoint = ConfigurationReader.get("getPetByIdEndPoint") + id;

        return petStoreRequestSpec(endpoint, null, null, null);
    }

    /**
     * Build request spec for updating pet
     * @param id Pet ID
     * @param updateName New pet name
     * @param tagName Tag name
     * @return RequestSpecification
     */
    public static RequestSpecification updatePetReqSpec(String id, String updateName, String tagName) {
        String endpoint = ConfigurationReader.get("updateNameEndPoint");
        String body = DataFactory_PetStore.updatePetBody(id, updateName, tagName);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for updating pet with custom status
     * @param id Pet ID
     * @param updateName New pet name
     * @param tagName Tag name
     * @param status Pet status
     * @return RequestSpecification
     */
    public static RequestSpecification updatePetReqSpec(String id, String updateName, String tagName, String status) {
        String endpoint = ConfigurationReader.get("updateNameEndPoint");
        String body = DataFactory_PetStore.updatePetBody(id, updateName, tagName, status);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for deleting pet by ID
     * @param id Pet ID
     * @return RequestSpecification
     */
    public static RequestSpecification deletePetReqSpec(String id) {
        String endpoint = ConfigurationReader.get("deleteByIdEndPoint") + id;

        return petStoreRequestSpec(endpoint, null, null, null);
    }

    /**
     * Build request spec for adding pet with minimal data (for testing)
     * @param id Pet ID
     * @param name Pet name
     * @return RequestSpecification
     */
    public static RequestSpecification addMinimalPetReqSpec(String id, String name) {
        String endpoint = ConfigurationReader.get("addNewPetEndPoint");
        String body = DataFactory_PetStore.minimalPetBody(id, name);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for adding invalid pet (negative testing)
     * @param id Pet ID
     * @return RequestSpecification
     */
    public static RequestSpecification addInvalidPetReqSpec(String id) {
        String endpoint = ConfigurationReader.get("addNewPetEndPoint");
        String body = DataFactory_PetStore.invalidPetBodyMissingPhotoUrls(id);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec for adding pet with multiple tags
     * @param id Pet ID
     * @param name Pet name
     * @param tags Array of tag names
     * @return RequestSpecification
     */
    public static RequestSpecification addPetWithTagsReqSpec(String id, String name, String[] tags) {
        String endpoint = ConfigurationReader.get("addNewPetEndPoint");
        String body = DataFactory_PetStore.petBodyWithMultipleTags(id, name, tags);

        return petStoreRequestSpec(endpoint, body, null, null);
    }

    /**
     * Build request spec with custom headers
     * Useful for testing authentication or special headers
     * @param id Pet ID
     * @param customHeaders Map of header name-value pairs
     * @return RequestSpecification
     */
    public static RequestSpecification getPetWithCustomHeaders(String id, Map<String, String> customHeaders) {
        String endpoint = ConfigurationReader.get("getPetByIdEndPoint") + id;

        // Don't cast, just pass it directly
        return petStoreRequestSpec(endpoint, null, null, null, customHeaders);
    }

    /**
     * Build request spec for getting pets by multiple statuses
     * @param statuses Array of statuses
     * @return RequestSpecification
     */
    public static RequestSpecification getPetByMultipleStatusesReqSpec(String[] statuses) {
        String endpoint = ConfigurationReader.get("getStatusEndPoint");

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("status", String.join(",", statuses));

        return petStoreRequestSpec(endpoint, null, queryParams, null);
    }
}