package org.example.models;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a shortened URL with associated metadata and functionality.
 * <p>
 * This class contains information about the original URL, the shortened URL,
 * its creator, click limits, and expiration details.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class ShortLink {

    /**
     * The timestamp when the link was created.
     */
    private final Instant creationTimestamp;

    /**
     * Indicates if the link is active.
     */
    private boolean isActive;

    /**
     * The current number of clicks on the shortened URL.
     */
    private int clickCount;

    /**
     * The shortened URL.
     */
    private final String shortenedUrl;

    /**
     * The original (long) URL.
     */
    private final String originalUrl;

    /**
     * The ID of the user who created the shortened link.
     */
    private final UUID userId;

    /**
     * The maximum number of clicks allowed for this shortened URL.
     */
    private int clickLimit;

    /**
     * The time-to-live (TTL) for the shortened link in milliseconds.
     */
    private final long timeToLiveMillis;

    /**
     * Constructs a new {@code ShortLink}.
     *
     * @param shortenedUrl     The shortened URL.
     * @param originalUrl      The original (long) URL.
     * @param userId           The ID of the user who created the shortened link.
     * @param clickLimit       The maximum number of clicks allowed for this shortened URL.
     * @param timeToLiveMillis The time-to-live (TTL) for the shortened link in milliseconds.
     */
    public ShortLink(String shortenedUrl, String originalUrl, UUID userId, int clickLimit, long timeToLiveMillis) {
        this.creationTimestamp = Instant.now();
        this.isActive = true;
        this.clickCount = 0;

        this.shortenedUrl = shortenedUrl;
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.clickLimit = clickLimit;
        this.timeToLiveMillis = timeToLiveMillis;
    }

    /**
     * Checks if the link is active.
     *
     * @return {@code true} if the link is active, {@code false} otherwise.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     * Disables the link, making it inactive.
     */
    public void disableLink() {
        this.isActive = false;
    }

    /**
     * Increments the click count each time the link is accessed.
     */
    public void incrementClicks() {
        clickCount++;
    }

    /**
     * Gets the shortened URL.
     *
     * @return The shortened URL.
     */
    public String getShortenedUrl() {
        return shortenedUrl;
    }

    /**
     * Gets the original (long) URL.
     *
     * @return The original URL.
     */
    public String getOriginalUrl() {
        return originalUrl;
    }

    /**
     * Gets the user ID associated with the shortened link.
     *
     * @return The user ID.
     */
    public UUID getUserId() {
        return userId;
    }

    /**
     * Sets the click limit for the shortened URL.
     *
     * @param clickLimit The new click limit.
     */
    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    /**
     * Checks if the click limit has been reached.
     *
     * @return {@code true} if the click limit has been reached, {@code false} otherwise.
     */
    public boolean isLimitReached() {
        return clickCount >= clickLimit;
    }

    /**
     * Checks if the link has expired based on its time-to-live (TTL).
     *
     * @return {@code true} if the link has expired, {@code false} otherwise.
     */
    public boolean isExpired() {
        return Instant.now().toEpochMilli() > (creationTimestamp.toEpochMilli() + timeToLiveMillis);
    }
}
