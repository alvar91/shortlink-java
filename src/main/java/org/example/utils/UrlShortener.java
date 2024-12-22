package org.example.utils;

import java.util.Random;

public class UrlShortener {
    // Base characters used for generating the shortened URL (alphanumeric characters)
    private static final String BASE_CHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Random object to generate random indices for the BASE_CHARS string
    private static final Random SALT = new Random();

    // Length of the generated shortened URL
    private static final int LENGTH = 6;

    // Method to generate a random 6-character short URL
    public static String generate() {
        // StringBuilder to efficiently build the shortened URL
        StringBuilder shortKey = new StringBuilder(LENGTH);

        // Loop to generate each character of the short URL
        for (int i = 0; i < LENGTH; i++) {
            // Append a random character from BASE_CHARS to the short URL
            shortKey.append(BASE_CHARS.charAt(SALT.nextInt(BASE_CHARS.length())));
        }

        // Return the generated shortened URL as a string
        return shortKey.toString();
    }
}
