package org.example.services;

import org.example.models.ShortLink;
import org.example.repositories.LinksRepository;
import org.example.utils.UrlShortener;

import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

public class LinkService {
    // Repository for managing links
    private final LinksRepository linksRepository;

    // Configuration service to retrieve configuration values
    private final ConfigService configService;

    // Constructor to initialize LinkService with repositories and configuration service
    public LinkService(LinksRepository repository, ConfigService configService) {
        this.linksRepository = repository;
        this.configService = configService;
    }

    // Method to create a shortened link
    public ShortLink createShortLink(UUID userId, String originalUrl, int clicksLimit, int lifetimeHours) {
        // Adjust the lifetime and clicks limit according to configuration settings
        int adjustedTtlHours = Math.min(lifetimeHours, configService.getMaxLifetimeHours()); // Ensure lifetime does not exceed the max limit
        int adjustedMaxClicks = Math.max(clicksLimit, configService.getMinClicksLimit()); // Ensure clicks limit meets the minimum threshold

        // Generate a unique code for the shortened URL
        String generatedCode = UrlShortener.generate();
        String generatedShortUrl = "http://clck.ru/" + generatedCode;

        // Convert lifetime in hours to milliseconds
        long ttlInMillis = Duration.ofHours(adjustedTtlHours).toMillis();

        // Create the ShortLink object and save it to the repository
        ShortLink shortLink = new ShortLink(generatedShortUrl, originalUrl, userId, adjustedMaxClicks, ttlInMillis);
        linksRepository.save(shortLink);

        return shortLink;
    }

    // Method to open the original URL using the shortened URL
    public void openLink(String shortUrl) {
        ShortLink shortLink = linksRepository.find(shortUrl);

        // If the link doesn't exist, notify the user
        if (shortLink == null) {
            System.out.println("Link not found");
            return;
        }

        // Check if the link is active, expired, or has exceeded the click limit
        if (!shortLink.isActive() || shortLink.isExpired() || shortLink.isLimitReached()) {
            System.out.println("The expiration date has passed, or the click limit has been reached");
            shortLink.disableLink();
            linksRepository.remove(shortUrl);
            System.out.println("The link has been deleted because it is unavailable");
            return;
        }

        // Increment the click count and try to open the original URL in the browser
        shortLink.incrementClicks();
        try {
            // If desktop support is available, open the link in the default browser
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(shortLink.getOriginalUrl()));
            } else {
                System.out.println("Open the link: " + shortLink.getOriginalUrl());
            }

        } catch (Exception e) {
            System.out.println("Failed to open the link in the browser: " + e.getMessage());
        }

        // If the click limit is reached, disable the link and remove it
        if (shortLink.isLimitReached()) {
            shortLink.disableLink();
            System.out.println("The click limit has been reached. The link has been disabled");
            linksRepository.remove(shortUrl);
            System.out.println("The link has been deleted");
        }
    }

    // Method to change the click limit for an existing shortened URL
    public boolean editLimit(UUID userId, String shortUrl, int newLimit) {
        ShortLink shortLink = linksRepository.find(shortUrl);

        // If the link doesn't exist, notify the user
        if (shortLink == null) {
            System.out.println("The link was not found");
            return false;
        }

        // Ensure the user is the owner of the link
        if (!shortLink.getUserId().equals(userId)) {
            System.out.println("You are not the owner of this link");
            return false;
        }

        // Adjust the new click limit based on the configuration
        int actualNewLimit = Math.max(newLimit, configService.getMinClicksLimit());

        // Update the click limit
        shortLink.setClickLimit(actualNewLimit);
        System.out.println("The click limit has been changed to: " + actualNewLimit);

        return true;
    }

    // Method to remove a shortened URL
    public boolean removeLink(UUID userId, String shortUrl) {
        ShortLink shortLink = linksRepository.find(shortUrl);

        // If the link doesn't exist, notify the user
        if (shortLink == null) {
            System.out.println("The link was not found");
            return false;
        }

        // Ensure the user is the owner of the link
        if (!shortLink.getUserId().equals(userId)) {
            System.out.println("You are not the owner of this link");
            return false;
        }

        // Remove the link from the repository
        linksRepository.remove(shortUrl);
        System.out.println("The link has been deleted");

        return true;
    }

    // Method to remove all expired links from the repository
    public void removeExpiredLinks() {
        // Remove links that are expired from the repository
        linksRepository.findAll().values().removeIf(ShortLink::isExpired);
        System.out.println("All expired links have been deleted");
    }
}
