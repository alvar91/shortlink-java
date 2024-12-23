package org.example.services;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Service class for loading and providing configuration settings.
 * <p>
 * The configuration is loaded from a {@code config.properties} file located in the classpath.
 * This class provides access to key application settings, such as the maximum lifetime
 * of shortened URLs and the minimum allowed click limit.
 * </p>
 * <p><b>Default Values:</b> If properties are not specified in the configuration file, default
 * values are used: 24 hours for maximum lifetime and 6 clicks for minimum click limit.</p>
 *
 * <p><b>Example Configuration File:</b></p>
 * <pre>{@code
 * maxLifetimeHours=48
 * clicksLimit=10
 * }</pre>
 *
 * @author alvar91
 * @version 1.0
 */
public class ConfigService {

    /**
     * The name of the configuration file.
     */
    private static final String CONFIG_FILE = "config.properties";

    /**
     * Key used to retrieve the maximum lifetime of shortened URLs from the configuration file.
     */
    private static final String MAX_LIFETIME_HOURS_KEY = "maxLifetimeHours";

    /**
     * Key used to retrieve the minimum click limit for shortened URLs from the configuration file.
     */
    private static final String CLICKS_LIMIT_KEY = "clicksLimit";

    /**
     * Properties object to hold configuration values.
     */
    private final Properties properties = new Properties();

    /**
     * Configured maximum lifetime of the shortened URL (in hours).
     */
    private final int maxLifetimeHours;

    /**
     * Configured minimum click limit for shortened URLs.
     */
    private final int minClicksLimit;

    /**
     * Constructor that loads configuration settings from the {@code config.properties} file.
     * <p>
     * If the file is not found or an error occurs while loading, an {@link IllegalStateException} is thrown.
     * Default values are used if specific properties are missing.
     * </p>
     */
    public ConfigService() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream == null) {
                throw new IllegalStateException(CONFIG_FILE + " not found in the classpath");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load configuration from: " + CONFIG_FILE, e);
        }

        this.maxLifetimeHours = parseProperty(MAX_LIFETIME_HOURS_KEY, 24);  // Default: 24 hours
        this.minClicksLimit = parseProperty(CLICKS_LIMIT_KEY, 6);           // Default: 6 clicks
    }

    /**
     * Parses an integer property from the configuration file.
     * <p>
     * If the property is not found or cannot be parsed, a default value is returned.
     * </p>
     *
     * @param key          The key of the property to parse.
     * @param defaultValue The default value to use if the property is missing or invalid.
     * @return The parsed integer value or the default value if parsing fails.
     */
    private int parseProperty(String key, int defaultValue) {
        return Integer.parseInt(properties.getProperty(key, String.valueOf(defaultValue)));
    }

    /**
     * Returns the configured maximum lifetime of shortened URLs in hours.
     *
     * @return The maximum lifetime in hours.
     */
    public int getMaxLifetimeHours() {
        return maxLifetimeHours;
    }

    /**
     * Returns the configured minimum click limit for shortened URLs.
     *
     * @return The minimum click limit.
     */
    public int getMinClicksLimit() {
        return minClicksLimit;
    }
}
