package org.example.repositories;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Repository class for managing registered users.
 * <p>
 * This class provides methods to add and verify users by their unique identifiers (UUIDs).
 * </p>
 *
 * <p>
 * It utilizes an {@link ArrayList} to store user IDs, which makes it simple and efficient
 * for applications with moderate user counts.
 * </p>
 *
 * <p><b>Note:</b> For large-scale systems, consider using a more efficient data structure
 * like a {@link java.util.HashSet} for constant-time lookups.</p>
 *
 * @author alvar91
 * @version 1.0
 */
public class UsersRepository {

    /**
     * A list to store the UUIDs of registered users.
     */
    private final ArrayList<UUID> usersId = new ArrayList<>();

    /**
     * Adds a new user by their unique identifier (UUID).
     *
     * @param userId The UUID of the user to be added.
     */
    public void addUser(UUID userId) {
        usersId.add(userId);
    }

    /**
     * Checks if a user exists in the repository by their UUID.
     *
     * @param userId The UUID of the user to check.
     * @return {@code true} if the user exists in the repository, {@code false} otherwise.
     */
    public boolean isUserExist(UUID userId) {
        return usersId.contains(userId);
    }
}
