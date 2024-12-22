package org.example.services;

import org.example.repositories.UsersRepository;

import java.util.UUID;

public class UserService {
    // Repository for managing users
    private final UsersRepository userRepository;

    // Constructor to initialize the UserService with the UsersRepository
    public UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Method to create a new user and return a new UUID for the user
    public UUID createUserId() {
        // Generate a unique UUID for the new user
        UUID newUser = UUID.randomUUID();

        // Add the new user to the repository
        userRepository.addUser(newUser);

        // Return the generated UUID for the new user
        return newUser;
    }

    // Method to check if a user already exists based on their UUID
    public boolean isUserExist(UUID userId) {
        // Check if the user ID exists in the repository
        return userRepository.isUserExist(userId);
    }
}
