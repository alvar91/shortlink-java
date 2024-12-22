package org.example.repositories;

import org.example.models.ShortLink;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class LinksRepository {
    // A thread-safe map to store the shortened links using the shortened URL as the key
    private final Map<String, ShortLink> links = new ConcurrentHashMap<>();

    // Finds and returns a ShortLink by its shortened URL
    public ShortLink find(String shortUrl) {
        return links.get(shortUrl);  // Returns the ShortLink object or null if not found
    }

    // Returns all stored ShortLinks as a map
    public Map<String, ShortLink> findAll() {
        return links;  // Returns the entire map of shortened links
    }

    // Saves a new ShortLink or updates an existing one based on the shortened URL
    public ShortLink save(ShortLink link) {
        links.put(link.getShortenedUrl(), link);  // Adds or replaces the ShortLink in the map
        return link;  // Returns the saved ShortLink
    }

    // Removes a ShortLink by its shortened URL
    public void remove(String shortUrl) {
        links.remove(shortUrl);  // Removes the ShortLink with the given shortened URL
    }
}
