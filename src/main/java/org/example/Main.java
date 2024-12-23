package org.example;

import org.example.controllers.ShortLinkController;

/**
 * The entry point of the application.
 * <p>
 * This class initializes and runs the {@link ShortLinkController}, which handles
 * user interactions and application operations.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class Main {
    /**
     * The main method, serving as the application entry point.
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        // Create an instance of the ShortLinkController
        ShortLinkController shortLinkController = new ShortLinkController();

        // Run the controller, which will handle user input and operations
        shortLinkController.run();
    }
}
