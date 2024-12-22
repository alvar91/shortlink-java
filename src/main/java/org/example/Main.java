package org.example;

import org.example.controllers.ShortLinkController;

public class Main {
    public static void main(String[] args) {
        // Create an instance of the ShortLinkController
        ShortLinkController shortLinkController = new ShortLinkController();

        // Run the controller, which will handle user input and operations
        shortLinkController.run();
    }
}
