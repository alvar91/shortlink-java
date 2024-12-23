package org.example.repositories;

import org.example.models.ShortLink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Repository class for managing shortened links.
 * <p>
 * This class provides a thread-safe mechanism for storing, retrieving,
 * updating, and removing {@link ShortLink} objects using their shortened URLs as keys.
 * </p>
 * <p>
 * By utilizing a {@link ConcurrentHashMap}, it ensures thread-safe access
 * in multi-threaded environments, making it suitable for concurrent applications.
 * </p>
 *
 * @author alvar91
 * @version 1.0
 */
public class LinksRepository {

    /**
     * A thread-safe map for storing shortened links, where the key is the shortened URL.
     */
    private final Map<String, ShortLink> links = new ConcurrentHashMap<>();

    /**
     * Finds a {@link ShortLink} by its shortened URL.
     *
     * @param shortUrl The shortened URL to search for.
     * @return The {@link ShortLink} object associated with the URL, or {@code null} if not found.
     */
    public ShortLink find(String shortUrl) {
        return links.get(shortUrl);
    }

    /**
     * Retrieves all stored shortened links.
     *
     * @return A map containing all {@link ShortLink} objects, with shortened URLs as keys.
     */
    public Map<String, ShortLink> findAll() {
        return links;
    }

    /**
     * Saves or updates a {@link ShortLink}.
     * <p>
     * If the shortened URL already exists, its associated {@link ShortLink} is replaced.
     * Otherwise, a new entry is created in the repository.
     * </p>
     *
     * @param link The {@link ShortLink} to save or update.
     * @return The saved {@link ShortLink}.
     */
    public ShortLink save(ShortLink link) {
        links.put(link.getShortenedUrl(), link);
        return link;
    }

    /**
     * Removes a {@link ShortLink} by its shortened URL.
     *
     * @param shortUrl The shortened URL to remove.
     */
    public void remove(String shortUrl) {
        links.remove(shortUrl);
    }
}
