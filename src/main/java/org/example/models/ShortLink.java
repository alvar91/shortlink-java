package org.example.models;

import java.time.Instant;
import java.util.UUID;

public class ShortLink {
    private final Instant creationTimestamp;  // The timestamp when the link was created
    private boolean isActive;  // Indicates if the link is active
    private int clickCount;  // The current number of clicks on the shortened URL

    private final String shortenedUrl;  // The shortened URL
    private final String originalUrl;  // The original (long) URL
    private final UUID userId;  // The ID of the user who created the shortened link
    private int clickLimit;  // The maximum number of clicks allowed for this shortened URL
    private final long timeToLiveMillis;  // The time-to-live (TTL) for the shortened link in milliseconds

    // Constructor to initialize the short link with necessary parameters
    public ShortLink(String shortenedUrl, String originalUrl, UUID userId, int clickLimit, long timeToLiveMillis) {
        this.creationTimestamp = Instant.now();  // Set the creation time of the link
        this.isActive = true;  // The link is active upon creation
        this.clickCount = 0;  // No clicks initially

        this.shortenedUrl = shortenedUrl;
        this.originalUrl = originalUrl;
        this.userId = userId;
        this.clickLimit = clickLimit;
        this.timeToLiveMillis = timeToLiveMillis;
    }

    // Returns whether the link is active
    public boolean isActive() {
        return isActive;
    }

    // Disables the link (sets it as inactive)
    public void disableLink() {
        this.isActive = false;
    }

    // Increments the click count each time the link is accessed
    public void incrementClicks() {
        clickCount++;
    }

    // Getter for shortened URL
    public String getShortenedUrl() {
        return shortenedUrl;
    }

    // Getter for the original (long) URL
    public String getOriginalUrl() {
        return originalUrl;
    }

    // Getter for the user ID associated with the shortened link
    public UUID getUserId() {
        return userId;
    }

    // Setter for the click limit
    public void setClickLimit(int clickLimit) {
        this.clickLimit = clickLimit;
    }

    // Checks if the click limit has been reached
    public boolean isLimitReached() {
        return clickCount >= clickLimit;
    }

    // Checks if the link has expired based on its TTL
    public boolean isExpired() {
        return Instant.now().toEpochMilli() > (creationTimestamp.toEpochMilli() + timeToLiveMillis);
    }
}
