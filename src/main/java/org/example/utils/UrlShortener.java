package org.example.utils;

import java.util.Random;

/**
 * Utility class for generating random shortened URLs.
 * <p>
 * This class provides a method to generate a random 6-character alphanumeric
 * string that can be used as part of a shortened URL.
 * </p>
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Generates a random 6-character alphanumeric string.</li>
 *   <li>Uses a predefined set of base characters (letters and digits) for generation.</li>
 * </ul>
 *
 * @author alvar91
 * @version 1.0
 */
public class UrlShortener {

    /**
     * The set of characters used for generating the shortened URL (alphanumeric).
     */
    private static final String BASE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Random object to generate random indices for the {@link #BASE_CHARS} string.
     */
    private static final Random SALT = new Random();

    /**
     * The length of the generated shortened URL (6 characters).
     */
    private static final int LENGTH = 6;

    /**
     * Generates a random 6-character string to be used as a shortened URL key.
     * <p>
     * This method randomly selects characters from the {@link #BASE_CHARS} set
     * to create a unique 6-character alphanumeric key.
     * </p>
     *
     * @return A randomly generated 6-character string to be used as a shortened URL.
     */
    public static String generate() {
        StringBuilder shortKey = new StringBuilder(LENGTH); // StringBuilder for efficient string concatenation

        // Loop to generate each character of the short URL
        for (int i = 0; i < LENGTH; i++) {
            // Append a random character from BASE_CHARS
            shortKey.append(BASE_CHARS.charAt(SALT.nextInt(BASE_CHARS.length())));
        }

        // Return the generated shortened URL key as a string
        return shortKey.toString();
    }
}
