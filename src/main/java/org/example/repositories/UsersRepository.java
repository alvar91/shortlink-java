package org.example.repositories;

import java.util.ArrayList;
import java.util.UUID;

public class UsersRepository {
    // A list to store the UUIDs of registered users
    private final ArrayList<UUID> usersId = new ArrayList<>();

    // Adds a new user by their UUID
    public void addUser(UUID userId) {
        usersId.add(userId);  // Adds the user UUID to the list
    }

    // Checks if a user exists by their UUID
    public boolean isUserExist(UUID userId) {
        return usersId.contains(userId);  // Returns true if the user exists in the list, otherwise false
    }
}
