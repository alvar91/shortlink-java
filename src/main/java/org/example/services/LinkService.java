package org.example.services;

import org.example.models.ShortLink;
import org.example.repositories.LinksRepository;
import org.example.utils.UrlShortener;

import java.awt.*;
import java.net.URI;
import java.time.Duration;
import java.util.UUID;

/**
 * Service class for managing shortened links.
 * <p>
 * Provides functionality for creating, editing, opening, and removing shortened links.
 * It also enforces rules and limits defined in the {@link ConfigService}.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Create shortened links with configurable lifetime and click limits.</li>
 *   <li>Open the original URL from a shortened link while respecting activity, expiration, and click limits.</li>
 *   <li>Edit click limits and manage link ownership.</li>
 *   <li>Remove expired or inactive links from the repository.</li>
 * </ul>
 *
 * @author alvar91
 * @version 1.0
 */
public class LinkService {

    /**
     * Repository for managing links.
     */
    private final LinksRepository linksRepository;

    /**
     * Configuration service to retrieve application settings.
     */
    private final ConfigService configService;

    /**
     * Constructor to initialize the LinkService with a repository and a configuration service.
     *
     * @param repository    The {@link LinksRepository} instance to manage links.
     * @param configService The {@link ConfigService} instance to enforce configuration rules.
     */
    public LinkService(LinksRepository repository, ConfigService configService) {
        this.linksRepository = repository;
        this.configService = configService;
    }

    /**
     * Creates a shortened link for a given user and URL.
     *
     * @param userId       The ID of the user creating the shortened link.
     * @param originalUrl  The original URL to be shortened.
     * @param clicksLimit  The maximum number of clicks allowed for the shortened link.
     * @param lifetimeHours The lifetime of the link in hours.
     * @return The created {@link ShortLink} object.
     */
    public ShortLink createShortLink(UUID userId, String originalUrl, int clicksLimit, int lifetimeHours) {
        int adjustedTtlHours = Math.min(lifetimeHours, configService.getMaxLifetimeHours());
        int adjustedMaxClicks = Math.max(clicksLimit, configService.getMinClicksLimit());
        String generatedCode = UrlShortener.generate();
        String generatedShortUrl = "http://clck.ru/" + generatedCode;
        long ttlInMillis = Duration.ofHours(adjustedTtlHours).toMillis();

        ShortLink shortLink = new ShortLink(generatedShortUrl, originalUrl, userId, adjustedMaxClicks, ttlInMillis);
        linksRepository.save(shortLink);

        return shortLink;
    }

    /**
     * Opens the original URL for a given shortened URL.
     * <p>
     * If the link is expired, inactive, or exceeds the click limit, it is disabled and removed.
     * </p>
     *
     * @param shortUrl The shortened URL to open.
     */
    public void openLink(String shortUrl) {
        ShortLink shortLink = linksRepository.find(shortUrl);
        if (shortLink == null) {
            System.out.println("Link not found");
            return;
        }

        if (!shortLink.isActive() || shortLink.isExpired() || shortLink.isLimitReached()) {
            System.out.println("The expiration date has passed, or the click limit has been reached");
            shortLink.disableLink();
            linksRepository.remove(shortUrl);
            System.out.println("The link has been deleted because it is unavailable");
            return;
        }

        shortLink.incrementClicks();
        try {
            if (Desktop.isDesktopSupported()) {
                Desktop.getDesktop().browse(URI.create(shortLink.getOriginalUrl()));
            } else {
                System.out.println("Open the link: " + shortLink.getOriginalUrl());
            }
        } catch (Exception e) {
            System.out.println("Failed to open the link in the browser: " + e.getMessage());
        }

        if (shortLink.isLimitReached()) {
            shortLink.disableLink();
            System.out.println("The click limit has been reached. The link has been disabled");
            linksRepository.remove(shortUrl);
            System.out.println("The link has been deleted");
        }
    }

    /**
     * Changes the click limit for a given shortened URL.
     *
     * @param userId   The ID of the user requesting the change.
     * @param shortUrl The shortened URL whose limit is to be changed.
     * @param newLimit The new click limit.
     * @return {@code true} if the limit was successfully updated; {@code false} otherwise.
     */
    public boolean editLimit(UUID userId, String shortUrl, int newLimit) {
        ShortLink shortLink = linksRepository.find(shortUrl);
        if (shortLink == null) {
            System.out.println("The link was not found");
            return false;
        }

        if (!shortLink.getUserId().equals(userId)) {
            System.out.println("You are not the owner of this link");
            return false;
        }

        int actualNewLimit = Math.max(newLimit, configService.getMinClicksLimit());
        shortLink.setClickLimit(actualNewLimit);
        System.out.println("The click limit has been changed to: " + actualNewLimit);

        return true;
    }

    /**
     * Removes a shortened URL from the repository.
     *
     * @param userId   The ID of the user requesting the removal.
     * @param shortUrl The shortened URL to be removed.
     * @return {@code true} if the link was successfully removed; {@code false} otherwise.
     */
    public boolean removeLink(UUID userId, String shortUrl) {
        ShortLink shortLink = linksRepository.find(shortUrl);
        if (shortLink == null) {
            System.out.println("The link was not found");
            return false;
        }

        if (!shortLink.getUserId().equals(userId)) {
            System.out.println("You are not the owner of this link");
            return false;
        }

        linksRepository.remove(shortUrl);
        System.out.println("The link has been deleted");

        return true;
    }

    /**
     * Removes all expired links from the repository.
     */
    public void removeExpiredLinks() {
        linksRepository.findAll().values().removeIf(ShortLink::isExpired);
        System.out.println("All expired links have been deleted");
    }
}
