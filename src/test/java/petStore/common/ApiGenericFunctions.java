package petStore.common;

import io.restassured.response.Response;
import org.junit.Assert;

/**
 * Generic utility functions for API testing
 * Provides common assertion and helper methods
 */
public class ApiGenericFunctions {

    /**
     * Verify HTTP status code matches expected value
     * @param expectedStatusCode Expected HTTP status code
     * @param actualStatusCode Actual HTTP status code from response
     */
    public static void verifyStatusCode(int expectedStatusCode, int actualStatusCode) {
        Assert.assertEquals(
                "Status code mismatch! Expected: " + expectedStatusCode + ", but got: " + actualStatusCode,
                expectedStatusCode,
                actualStatusCode
        );
    }

    /**
     * Verify response body content matches expected value
     * @param expectedBody Expected response body
     * @param actualBody Actual response body
     */
    public static void verifyResponseBody(String expectedBody, String actualBody) {
        Assert.assertEquals(
                "Response body mismatch! Expected: " + expectedBody + ", but got: " + actualBody,
                expectedBody,
                actualBody
        );
    }

    /**
     * Verify a specific JSON path value in the response
     * @param expectedKey JSON path key to verify
     * @param expectedValue Expected value at that path
     * @param response REST Assured Response object
     */
    public static void verifyResponsePathKeyValue(String expectedKey, String expectedValue, Response response) {
        try {
            String actualValue = response.path(expectedKey);
            Assert.assertEquals(
                    "Path value mismatch for key '" + expectedKey + "'! Expected: " + expectedValue + ", but got: " + actualValue,
                    expectedValue,
                    actualValue
            );
        } catch (Exception e) {
            Assert.fail("Failed to extract path '" + expectedKey + "' from response: " + e.getMessage());
        }
    }

    /**
     * Verify response contains a specific key
     * @param key JSON path key to check
     * @param response REST Assured Response object
     */
    public static void verifyResponseContainsKey(String key, Response response) {
        try {
            Object value = response.path(key);
            Assert.assertNotNull("Response does not contain key: " + key, value);
        } catch (Exception e) {
            Assert.fail("Key '" + key + "' not found in response: " + e.getMessage());
        }
    }

    /**
     * Verify response time is within acceptable limit
     * @param maxTimeInMs Maximum acceptable response time in milliseconds
     * @param response REST Assured Response object
     */
    public static void verifyResponseTime(long maxTimeInMs, Response response) {
        long actualTime = response.getTime();
        Assert.assertTrue(
                "Response time exceeded! Expected < " + maxTimeInMs + "ms, but got: " + actualTime + "ms",
                actualTime < maxTimeInMs
        );
    }

    /**
     * Verify response is not null
     * @param response REST Assured Response object
     */
    public static void verifyResponseNotNull(Response response) {
        Assert.assertNotNull("Response is null!", response);
    }

    /**
     * Verify response body is not empty
     * @param response REST Assured Response object
     */
    public static void verifyResponseBodyNotEmpty(Response response) {
        String body = response.getBody().asString();
        Assert.assertFalse("Response body is empty!", body == null || body.isEmpty());
    }

    /**
     * Verify content type header
     * @param expectedContentType Expected content type (e.g., "application/json")
     * @param response REST Assured Response object
     */
    public static void verifyContentType(String expectedContentType, Response response) {
        String actualContentType = response.getContentType();
        Assert.assertTrue(
                "Content type mismatch! Expected: " + expectedContentType + ", but got: " + actualContentType,
                actualContentType != null && actualContentType.contains(expectedContentType)
        );
    }

    /**
     * Verify a header exists in the response
     * @param headerName Name of the header to verify
     * @param response REST Assured Response object
     */
    public static void verifyHeaderExists(String headerName, Response response) {
        String headerValue = response.getHeader(headerName);
        Assert.assertNotNull("Header '" + headerName + "' not found in response!", headerValue);
    }

    /**
     * Wait for specified number of seconds
     * Useful for waiting between API calls or for data propagation
     * @param seconds Number of seconds to wait
     */
    public static void waitForSec(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
            System.out.println("⏱️ Waited for " + seconds + " second(s)");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("Wait interrupted: " + e.getMessage());
        }
    }

    /**
     * Log response details for debugging
     * @param response REST Assured Response object
     */
    public static void logResponse(Response response) {
        System.out.println("========== RESPONSE DETAILS ==========");
        System.out.println("Status Code: " + response.getStatusCode());
        System.out.println("Content Type: " + response.getContentType());
        System.out.println("Response Time: " + response.getTime() + "ms");
        System.out.println("Response Body: " + response.getBody().asString());
        System.out.println("======================================");
    }

    /**
     * Extract and return a value from JSON path
     * @param key JSON path key
     * @param response REST Assured Response object
     * @return Value as String, or null if not found
     */
    public static String extractValue(String key, Response response) {
        try {
            return response.path(key);
        } catch (Exception e) {
            System.err.println("Could not extract value for key '" + key + "': " + e.getMessage());
            return null;
        }
    }
}