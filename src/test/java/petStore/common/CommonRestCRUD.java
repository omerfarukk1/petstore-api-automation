package petStore.common;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

/**
 * Common REST API CRUD operations wrapper
 * Provides reusable methods for HTTP requests with RestAssured
 */
public class CommonRestCRUD {

    /**
     * Perform GET request
     * @param requestSpecification Request specification with base URI, headers, etc.
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response get(RequestSpecification requestSpecification, int expectedResponseCode) {
        System.out.println("🔵 Executing GET request...");
        return given().spec(requestSpecification)
                .when().log().all()
                .get()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform GET request without status code validation
     * Use this when you want to validate status code separately
     * @param requestSpecification Request specification
     * @return Response object
     */
    public static Response get(RequestSpecification requestSpecification) {
        System.out.println("🔵 Executing GET request (no status validation)...");
        return given().spec(requestSpecification)
                .when().log().all()
                .get()
                .then().log().all()
                .extract().response();
    }

    /**
     * Perform POST request
     * @param requestSpecification Request specification with body, headers, etc.
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response post(RequestSpecification requestSpecification, int expectedResponseCode) {
        System.out.println("🟢 Executing POST request...");
        return given().spec(requestSpecification)
                .when().log().all()
                .post()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform POST request without status code validation
     * @param requestSpecification Request specification
     * @return Response object
     */
    public static Response post(RequestSpecification requestSpecification) {
        System.out.println("🟢 Executing POST request (no status validation)...");
        return given().spec(requestSpecification)
                .when().log().all()
                .post()
                .then().log().all()
                .extract().response();
    }

    /**
     * Perform PUT request
     * @param requestSpecification Request specification with body, headers, etc.
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response put(RequestSpecification requestSpecification, int expectedResponseCode) {
        System.out.println("🟡 Executing PUT request...");
        return given().spec(requestSpecification)
                .when().log().all()
                .put()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform PUT request without status code validation
     * @param requestSpecification Request specification
     * @return Response object
     */
    public static Response put(RequestSpecification requestSpecification) {
        System.out.println("🟡 Executing PUT request (no status validation)...");
        return given().spec(requestSpecification)
                .when().log().all()
                .put()
                .then().log().all()
                .extract().response();
    }

    /**
     * Perform PATCH request
     * @param requestSpecification Request specification with body, headers, etc.
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response patch(RequestSpecification requestSpecification, int expectedResponseCode) {
        System.out.println("🟠 Executing PATCH request...");
        return given().spec(requestSpecification)
                .when().log().all()
                .patch()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform PATCH request without status code validation
     * @param requestSpecification Request specification
     * @return Response object
     */
    public static Response patch(RequestSpecification requestSpecification) {
        System.out.println("🟠 Executing PATCH request (no status validation)...");
        return given().spec(requestSpecification)
                .when().log().all()
                .patch()
                .then().log().all()
                .extract().response();
    }

    /**
     * Perform DELETE request
     * @param requestSpecification Request specification
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response delete(RequestSpecification requestSpecification, int expectedResponseCode) {
        System.out.println("🔴 Executing DELETE request...");
        return given().spec(requestSpecification)
                .when().log().all()
                .delete()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform DELETE request without status code validation
     * @param requestSpecification Request specification
     * @return Response object
     */
    public static Response delete(RequestSpecification requestSpecification) {
        System.out.println("🔴 Executing DELETE request (no status validation)...");
        return given().spec(requestSpecification)
                .when().log().all()
                .delete()
                .then().log().all()
                .extract().response();
    }

    /**
     * Perform GET request with path parameters
     * @param requestSpecification Request specification
     * @param pathParam Path parameter value (e.g., pet ID)
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response getWithPathParam(RequestSpecification requestSpecification, String pathParam, int expectedResponseCode) {
        System.out.println("🔵 Executing GET request with path param: " + pathParam);
        return given().spec(requestSpecification)
                .pathParam("id", pathParam)
                .when().log().all()
                .get("/{id}")
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform GET request with query parameters
     * @param requestSpecification Request specification
     * @param queryParamName Query parameter name
     * @param queryParamValue Query parameter value
     * @param expectedResponseCode Expected HTTP status code
     * @return Response object
     */
    public static Response getWithQueryParam(RequestSpecification requestSpecification,
                                             String queryParamName,
                                             String queryParamValue,
                                             int expectedResponseCode) {
        System.out.println("🔵 Executing GET request with query param: " + queryParamName + "=" + queryParamValue);
        return given().spec(requestSpecification)
                .queryParam(queryParamName, queryParamValue)
                .when().log().all()
                .get()
                .then().log().all()
                .statusCode(expectedResponseCode)
                .extract().response();
    }

    /**
     * Perform request with custom endpoint
     * Useful when you need to hit a different endpoint than the base
     * @param requestSpecification Request specification
     * @param endpoint Custom endpoint path
     * @param method HTTP method (GET, POST, PUT, DELETE)
     * @return Response object
     */
    public static Response executeRequest(RequestSpecification requestSpecification, String endpoint, String method) {
        System.out.println("🔷 Executing " + method + " request to endpoint: " + endpoint);
        Response response = null;

        switch (method.toUpperCase()) {
            case "GET":
                response = given().spec(requestSpecification)
                        .when().log().all()
                        .get(endpoint)
                        .then().log().all()
                        .extract().response();
                break;
            case "POST":
                response = given().spec(requestSpecification)
                        .when().log().all()
                        .post(endpoint)
                        .then().log().all()
                        .extract().response();
                break;
            case "PUT":
                response = given().spec(requestSpecification)
                        .when().log().all()
                        .put(endpoint)
                        .then().log().all()
                        .extract().response();
                break;
            case "DELETE":
                response = given().spec(requestSpecification)
                        .when().log().all()
                        .delete(endpoint)
                        .then().log().all()
                        .extract().response();
                break;
            default:
                throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        return response;
    }
}