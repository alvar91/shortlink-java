package org.example.controllers;

import org.example.models.ShortLink;
import org.example.repositories.LinksRepository;
import org.example.repositories.UsersRepository;
import org.example.services.ConfigService;
import org.example.services.LinkService;
import org.example.services.UserService;

import java.util.Scanner;
import java.util.UUID;

/**
 * Controller for managing the URL shortening service.
 * <p>
 * This class handles user commands, interacts with the services and repositories,
 * and provides the main loop for the application's operation.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class ShortLinkController {

    /**
     * The current user ID.
     */
    UUID userId = null;

    /**
     * Scanner for reading user input.
     */
    Scanner scanner = new Scanner(System.in);

    // Repositories and services for managing users and links

    /**
     * Repository for managing user data.
     */
    UsersRepository usersRepository = new UsersRepository();

    /**
     * Repository for managing link data.
     */
    LinksRepository linksRepository = new LinksRepository();

    /**
     * Service for configuration management.
     */
    ConfigService configService = new ConfigService();

    /**
     * Service for user management.
     */
    UserService userService = new UserService(usersRepository);

    /**
     * Service for link management.
     */
    LinkService linkService = new LinkService(linksRepository, configService);

    /**
     * Displays the help menu with a list of available commands.
     */
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

    /**
     * Registers a new user and logs them in.
     */
    private void showRegisterMenu() {
        UUID newUserId = userService.createUserId();
        userId = newUserId;

        System.out.println("A new user has been registered with UUID: " + newUserId);
        System.out.println("You have logged in as a user: " + userId);
    }

    /**
     * Logs in an existing user using their UUID.
     *
     * @param chunks Input split into command parts.
     */
    private void showLoginMenu(String[] chunks) {
        if (chunks.length < 2) {
            System.out.println("Usage: login UUID");
            return;
        }

        try {
            UUID loginUserId = UUID.fromString(chunks[1]);
            if (userService.isUserExist(loginUserId)) {
                userId = loginUserId;
                System.out.println("You have logged in as user:" + userId);
                return;
            }

            System.out.println("A user with this UUID is not registered");
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid UUID");
        }
    }

    /**
     * Prints a message when no user is logged in.
     */
    private void printNoUserId() {
        System.out.println("Please register or login");
    }

    /**
     * Handles shortening a URL.
     *
     * @param chunks Input split into command parts.
     */
    private void showShortMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();
        }
        if (chunks.length < 4) {
            System.out.println("Incorrect input format: short url clicksLimit lifetimeHours");
            return;
        }

        try {
            String url = chunks[1];
            int clicksLimit = Integer.parseInt(chunks[2]);
            int lifetimeHours = Integer.parseInt(chunks[3]);

            ShortLink shortLink = linkService.createShortLink(userId, url, clicksLimit, lifetimeHours);
            System.out.println("Shortened link: " + shortLink.getShortenedUrl());
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for clicksLimit or lifetimeHours");
        }
    }

    /**
     * Handles opening a shortened URL.
     *
     * @param chunks Input split into command parts.
     */
    private void showOpenMenu(String[] chunks) {
        if (chunks.length < 2) {
            System.out.println("Incorrect input format: open shortUrl");
            return;
        }

        linkService.openLink(chunks[1]);
    }

    /**
     * Handles changing the click limit for a shortened URL.
     *
     * @param chunks Input split into command parts.
     */
    private void showEditClicksMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();
        }

        if (chunks.length < 3) {
            System.out.println("Incorrect input format: editClicksLimit shortUrl newLimit");
            return;
        }

        try {
            String shortUrl = chunks[1];
            int newLimit = Integer.parseInt(chunks[2]);

            linkService.editLimit(userId, shortUrl, newLimit);
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format for shortUrl or newLimit");
        }
    }

    /**
     * Handles removing a shortened URL.
     *
     * @param chunks Input split into command parts.
     */
    private void showRemoveMenu(String[] chunks) {
        if (userId == null) {
            printNoUserId();
        }

        if (chunks.length < 2) {
            System.out.println("Incorrect input format: remove shortUrl");
            return;
        }

        String shortUrl = chunks[1];
        linkService.removeLink(userId, shortUrl);
    }

    /**
     * Handles clearing expired links.
     */
    private void showClearMenu() {
        linkService.removeExpiredLinks();
        System.out.println("Expired links have been removed");
    }

    /**
     * Main loop for handling user input and executing commands.
     */
    public void run() {
        System.out.println("Welcome to the URL Shortening Service!");
        System.out.println("To view the available commands, enter the command help");

        boolean isRunning = true;
        while (isRunning) {
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) continue;

            String[] chunks = input.split("\\s+");
            String action = chunks[0].toLowerCase();

            switch (action) {
                case "help":
                    showHelpMenu();
                    break;
                case "exit":
                    isRunning = false;
                    break;
                case "register":
                    showRegisterMenu();
                    break;
                case "login":
                    showLoginMenu(chunks);
                    break;
                case "short":
                    showShortMenu(chunks);
                    break;
                case "open":
                    showOpenMenu(chunks);
                    break;
                case "edit_clicks_limit":
                    showEditClicksMenu(chunks);
                    break;
                case "remove":
                    showRemoveMenu(chunks);
                    break;
                case "clear":
                    showClearMenu();
                    break;
                default:
                    System.out.println("Unknown command. Type help for a list of commands");
            }
        }
        scanner.close();
    }
}
