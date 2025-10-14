package petStore.specs;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import petStore.common.ConfigurationReader;

import java.util.Collections;
import java.util.Map;

/**
 * Common Request Specification Builder
 * Provides base request specifications for all API services
 */
public class CommonRequestSpec {

    /**
     * Build base request specification with URI and logging filters
     * @param uri Base URI for the request
     * @return RequestSpecification with base configuration
     */
    protected static RequestSpecification buildBaseSpec(String uri) {
        return new RequestSpecBuilder()
                .setBaseUri(uri)
                .addFilter(new RequestLoggingFilter())   // Log request details
                .addFilter(new ResponseLoggingFilter())  // Log response details
                .build();
    }

    /**
     * Build complete request specification for PetStore API (4 parameters)
     * @param endpoint API endpoint path
     * @param body Request body (JSON string or object)
     * @param queryParams Query parameters map
     * @param pathParams Path parameters map
     * @return Complete RequestSpecification
     */
    protected static RequestSpecification petStoreRequestSpec(
            String endpoint,
            Object body,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams) {

        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParams(queryParams != null ? queryParams : Collections.emptyMap())
                .addPathParams(pathParams != null ? pathParams : Collections.emptyMap())
                .setBody(body != null ? body : "")
                .build();
    }

    /**
     * Build request specification with custom headers (5 parameters)
     * @param endpoint API endpoint path
     * @param body Request body
     * @param queryParams Query parameters
     * @param pathParams Path parameters
     * @param headers Custom headers map
     * @return RequestSpecification with custom headers
     */
    protected static RequestSpecification petStoreRequestSpec(
            String endpoint,
            Object body,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams,
            Map<String, String> headers) {

        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        RequestSpecBuilder builder = new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParams(queryParams != null ? queryParams : Collections.emptyMap())
                .addPathParams(pathParams != null ? pathParams : Collections.emptyMap())
                .setBody(body != null ? body : "");

        // Add custom headers if provided
        if (headers != null && !headers.isEmpty()) {
            builder.addHeaders(headers);
        }

        return builder.build();
    }

    /**
     * Build request specification without logging (for performance testing)
     * @param endpoint API endpoint path
     * @param body Request body
     * @param queryParams Query parameters
     * @param pathParams Path parameters
     * @return RequestSpecification without logging filters
     */
    protected static RequestSpecification petStoreRequestSpecNoLogging(
            String endpoint,
            Object body,
            Map<String, Object> queryParams,
            Map<String, Object> pathParams) {

        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .setBaseUri(fullUri)
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addQueryParams(queryParams != null ? queryParams : Collections.emptyMap())
                .addPathParams(pathParams != null ? pathParams : Collections.emptyMap())
                .setBody(body != null ? body : "")
                .build();
    }

    /**
     * Build minimal request specification (GET requests with no body)
     * @param endpoint API endpoint path
     * @return Minimal RequestSpecification
     */
    protected static RequestSpecification petStoreRequestSpecMinimal(String endpoint) {
        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .build();
    }

    /**
     * Build request specification with authentication token
     * @param endpoint API endpoint path
     * @param body Request body
     * @param authToken Bearer token for authentication
     * @return RequestSpecification with Authorization header
     */
    protected static RequestSpecification petStoreRequestSpecWithAuth(
            String endpoint,
            Object body,
            String authToken) {

        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("Authorization", "Bearer " + authToken)
                .setBody(body != null ? body : "")
                .build();
    }

    /**
     * Build request specification with API key
     * @param endpoint API endpoint path
     * @param apiKey API key for authentication
     * @return RequestSpecification with api_key header
     */
    protected static RequestSpecification petStoreRequestSpecWithApiKey(
            String endpoint,
            String apiKey) {

        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .addHeader("api_key", apiKey)
                .build();
    }

    /**
     * Build request specification for multipart/form-data (file uploads)
     * @param endpoint API endpoint path
     * @return RequestSpecification for multipart requests
     */
    protected static RequestSpecification petStoreRequestSpecMultipart(String endpoint) {
        String fullUri = ConfigurationReader.get("baseUri") + endpoint;

        return new RequestSpecBuilder()
                .addRequestSpecification(buildBaseSpec(fullUri))
                .setContentType(ContentType.MULTIPART)
                .build();
    }
}