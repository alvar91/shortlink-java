package org.example.controllers;

import org.example.models.ShortLink;
import org.example.repositories.LinksRepository;
import org.example.repositories.UsersRepository;
import org.example.services.ConfigService;
import org.example.services.LinkService;
import org.example.services.UserService;

import java.util.Scanner;
import java.util.UUID;

public class ShortLinkController {
    UUID userId = null;  // The current user ID

    Scanner scanner = new Scanner(System.in);  // Scanner for reading user input

    // Repositories for users and links
    UsersRepository usersRepository = new UsersRepository();
    LinksRepository linksRepository = new LinksRepository();

    // Services for managing users, links, and configuration
    ConfigService configService = new ConfigService();
    UserService userService = new UserService(usersRepository);
    LinkService linkService = new LinkService(linksRepository, configService);

    // Displays the help menu with available commands
    private void showHelpMenu() {
        System.out.println("exit: exit the program");
        System.out.println("register: create a new user");
        System.out.println("login UUID: login with an existing user");
        System.out.println("short url clicksLimit lifetimeHours: shorten a link");
        System.out.println("open shortUrl: open a shortened link");
        System.out.println("edit_clicks_limit shortUrl newLimit: change the redirect limit");
        System.out.println("remove shortUrl: remove a link");
        System.out.println("clear: remove expired links");
    }

    // Registers a new user and logs them in
    private void showRegisterMenu() {
        UUID newUserId = userService.createUserId();  // Create a new user ID
        userId = newUserId;  // Set the new user ID as the current user

        System.out.println("A new user has been registered with UUID: " + newUserId);
        System.out.println("You have logged in as a user: " + userId);
    }

    // Logs in an existing user using their UUID
    private void showLoginMenu(String[] chunks) {
        if (chunks.length < 2) {
            System.out.println("Usage: login UUID");
            return;
        }

        try {
            UUID loginUserId = UUID.fromString(chunks[1]);  // Convert the input string to UUID

            if (userService.isUserExist(loginUserId)) {
                userId = loginUserId;  // Log in the user
                System.out.println("You have logged in as user:" + userId);
                return;
            }

            System.out.println("A user with this UUID is not registered");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID");
        }
    }

    // Prints a message when no user is logged in
    private void printNoUserId() {
        System.out.println("Please register or login");
    }

    // Handles shortening a URL
    private void showShortMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();  // Ensure the user is logged in
        }
        if (chunks.length < 4) {
            System.out.println("Incorrect input format: short url clicksLimit lifetimeHours");
            return;
        }

        try {
            String url = chunks[1];  // The URL to shorten
            int clicksLimit = Integer.parseInt(chunks[2]);  // The click limit
            int lifetimeHours = Integer.parseInt(chunks[3]);  // The lifetime in hours

            // Create the shortened URL
            ShortLink shortLink = linkService.createShortLink(userId, url, clicksLimit, lifetimeHours);
            System.out.println("Shortened link: " + shortLink.getShortenedUrl());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for clicksLimit or lifetimeHours");
        }
    }

    // Handles opening a shortened URL
    private void showOpenMenu(String[] chunks) {
        if (chunks.length < 2) {
            System.out.println("Incorrect input format: open shortUrl");
            return;
        }

        linkService.openLink(chunks[1]);  // Open the shortened URL
    }

    // Handles changing the click limit for a shortened URL
    private void showEditClicksMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();  // Ensure the user is logged in
        }

        if (chunks.length < 3) {
            System.out.println("Incorrect input format: editClicksLimit shortUrl newLimit");
            return;
        }

        try {
            String shortUrl = chunks[1];  // The shortened URL
            int newLimit = Integer.parseInt(chunks[2]);  // The new click limit

            linkService.editLimit(userId, shortUrl, newLimit);  // Update the click limit
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for shortUrl or newLimit");
        }
    }

    // Handles removing a shortened URL
    private void showRemoveMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();  // Ensure the user is logged in
        }

        if (chunks.length < 2) {
            System.out.println("Incorrect input format: remove shortUrl");
            return;
        }

        String shortUrl = chunks[1];  // The shortened URL
        linkService.removeLink(userId, shortUrl);  // Remove the link
    }

    // Handles clearing expired links
    private void showClearMenu() {
        linkService.removeExpiredLinks();  // Remove all expired links
        System.out.println("Expired links have been removed");
    }

    // Main loop for handling user input and executing commands
    public void run() {
        System.out.println("Welcome to the URL Shortening Service!");
        System.out.println("To view the available commands, enter the command help");

        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine().trim();  // Read user input

            if (input.isEmpty()) continue;

            String[] chunks = input.split("\\s+");
            String action = chunks[0].toLowerCase();  // Get the action (command)

            // Process the action based on the command entered by the user
            switch (action) {
                case "help":
                    showHelpMenu();  // Show available commands
                    break;
                case "exit":
                    isRunning = false;  // Exit the program
                    break;
                case "register":
                    showRegisterMenu();  // Register a new user
                    break;
                case "login":
                    showLoginMenu(chunks);  // Log in an existing user
                    break;
                case "short":
                    showShortMenu(chunks);  // Shorten a URL
                    break;
                case "open":
                    showOpenMenu(chunks);  // Open a shortened URL
                    break;
                case "edit_clicks_limit":
                    showEditClicksMenu(chunks);  // Edit the click limit for a link
                    break;
                case "remove":
                    showRemoveMenu(chunks);  // Remove a shortened URL
                    break;
                case "clear":
                    showClearMenu();  // Remove expired links
                    break;
                default:
                    System.out.println("Unknown command. Type help for a list of commands");
            }
        }
        scanner.close();  // Close the scanner when done
    }
}
