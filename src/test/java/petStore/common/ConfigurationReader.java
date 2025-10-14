package petStore.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Reader - Loads properties from configuration file
 * Supports both file system and classpath loading
 */
public class ConfigurationReader {

    private static Properties properties;
    private static final String CONFIG_FILE = "configuration.properties";

    static {
        properties = new Properties();
        loadProperties();
    }

    /**
     * Load properties from configuration file
     * First tries to load from classpath, then from file system
     */
    private static void loadProperties() {
        InputStream input = null;

        try {
            // Try loading from classpath first (src/test/resources)
            input = ConfigurationReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE);

            if (input != null) {
                properties.load(input);
                System.out.println("✓ Configuration loaded from classpath: " + CONFIG_FILE);
            } else {
                // Fallback: Try loading from file system
                input = new FileInputStream(CONFIG_FILE);
                properties.load(input);
                System.out.println("✓ Configuration loaded from file system: " + CONFIG_FILE);
            }

        } catch (IOException e) {
            System.err.println("❌ ERROR: Could not load configuration file: " + CONFIG_FILE);
            System.err.println("Please ensure the file exists in src/test/resources/ or project root");
            e.printStackTrace();
            throw new RuntimeException("Configuration file not found: " + CONFIG_FILE, e);
        } finally {
            closeInputStream(input);
        }
    }

    /**
     * Get property value by key
     * @param keyName Property key
     * @return Property value, or null if not found
     */
    public static String get(String keyName) {
        String value = properties.getProperty(keyName);

        if (value == null) {
            System.err.println("⚠️ WARNING: Property '" + keyName + "' not found in configuration");
        }

        return value;
    }

    /**
     * Get property value with default fallback
     * @param keyName Property key
     * @param defaultValue Default value if key not found
     * @return Property value or default value
     */
    public static String get(String keyName, String defaultValue) {
        String value = properties.getProperty(keyName);

        if (value == null) {
            System.out.println("ℹ️ Property '" + keyName + "' not found, using default: " + defaultValue);
            return defaultValue;
        }

        return value;
    }

    /**
     * Get property as integer
     * @param keyName Property key
     * @return Integer value
     * @throws NumberFormatException if value cannot be parsed
     */
    public static int getInt(String keyName) {
        String value = get(keyName);

        if (value == null) {
            throw new IllegalArgumentException("Property '" + keyName + "' not found");
        }

        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Property '" + keyName + "' value '" + value + "' is not a valid integer");
        }
    }

    /**
     * Get property as integer with default value
     * @param keyName Property key
     * @param defaultValue Default value if key not found or invalid
     * @return Integer value or default
     */
    public static int getInt(String keyName, int defaultValue) {
        try {
            return getInt(keyName);
        } catch (Exception e) {
            System.out.println("ℹ️ Using default integer value for '" + keyName + "': " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Get property as boolean
     * @param keyName Property key
     * @return Boolean value
     */
    public static boolean getBoolean(String keyName) {
        String value = get(keyName);

        if (value == null) {
            throw new IllegalArgumentException("Property '" + keyName + "' not found");
        }

        return Boolean.parseBoolean(value);
    }

    /**
     * Get property as boolean with default value
     * @param keyName Property key
     * @param defaultValue Default value if key not found
     * @return Boolean value or default
     */
    public static boolean getBoolean(String keyName, boolean defaultValue) {
        try {
            return getBoolean(keyName);
        } catch (Exception e) {
            System.out.println("ℹ️ Using default boolean value for '" + keyName + "': " + defaultValue);
            return defaultValue;
        }
    }

    /**
     * Check if a property exists
     * @param keyName Property key
     * @return true if property exists, false otherwise
     */
    public static boolean hasProperty(String keyName) {
        return properties.containsKey(keyName);
    }

    /**
     * Get all properties
     * @return Properties object
     */
    public static Properties getAllProperties() {
        return properties;
    }

    /**
     * Print all configuration properties (for debugging)
     */
    public static void printAllProperties() {
        System.out.println("========== CONFIGURATION PROPERTIES ==========");
        properties.forEach((key, value) -> System.out.println(key + " = " + value));
        System.out.println("==============================================");
    }

    /**
     * Safely close input stream
     * @param input InputStream to close
     */
    private static void closeInputStream(InputStream input) {
        if (input != null) {
            try {
                input.close();
            } catch (IOException e) {
                System.err.println("Warning: Could not close input stream: " + e.getMessage());
            }
        }
    }
}