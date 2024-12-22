package org.example.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigService {
    // The name of the configuration file
    private static final String CONFIG_FILE = "config.properties";

    // Keys used to access specific configuration properties
    private static final String MAX_LIFETIME_HOURS_KEY = "maxLifetimeHours";
    private static final String CLICKS_LIMIT_KEY = "clicksLimit";

    // Properties object to hold the configuration values
    private final Properties properties = new Properties();

    // Configured maximum lifetime of the shortened URL (in hours)
    private final int maxLifetimeHours;

    // Configured minimum number of clicks allowed for the shortened URL
    private final int minClicksLimit;

    public ConfigService() {
        // Try to load the configuration file
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            // Check if the file is found
            if (inputStream == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found in the classpath");
            }

            // Load the properties from the configuration file
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration from: " + CONFIG_FILE, e);
        }

        // Set the values for max lifetime and min clicks limit from the properties or use default values
        this.maxLifetimeHours = parseProperty(MAX_LIFETIME_HOURS_KEY, 24);  // Default is 24 hours
        this.minClicksLimit = parseProperty(CLICKS_LIMIT_KEY, 6);            // Default is 6 clicks
    }

    // Helper method to parse an integer property with a default value
    private int parseProperty(String key, int defaultValue) {
        // Attempt to read the property value, if not found, use the default value
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    // Getter method for maxLifetimeHours
    public int getMaxLifetimeHours() {
        return maxLifetimeHours;
    }

    // Getter method for minClicksLimit
    public int getMinClicksLimit() {
        return minClicksLimit;
    }
}
