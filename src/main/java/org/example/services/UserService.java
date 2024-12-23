package org.example.services;

import org.example.repositories.UsersRepository;

import java.util.UUID;

/**
 * Service class for managing user operations.
 * <p>
 * Provides functionality to create new users and check user existence
 * by interacting with the {@link UsersRepository}.
 * </p>
 *
 * <p><b>Key Features:</b></p>
 * <ul>
 *   <li>Generate unique identifiers for new users.</li>
 *   <li>Verify user existence in the system.</li>
 * </ul>
 *
 * @author alvar91
 * @version 1.0
 */
public class UserService {

    /**
     * Repository for managing user information.
     */
    private final UsersRepository userRepository;

    /**
     * Constructor to initialize the {@code UserService} with a {@link UsersRepository}.
     *
     * @param userRepository The {@link UsersRepository} instance for managing users.
     */
    public UserService(UsersRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Creates a new user and returns a unique {@link UUID} for the user.
     * <p>
     * The generated UUID is added to the repository to track the user's existence.
     * </p>
     *
     * @return The generated {@link UUID} for the newly created user.
     */
    public UUID createUserId() {
        UUID newUser = UUID.randomUUID(); // Generate a unique UUID
        userRepository.addUser(newUser); // Add the new user to the repository
        return newUser;
    }

    /**
     * Checks if a user exists in the system based on their {@link UUID}.
     *
     * @param userId The {@link UUID} of the user to check.
     * @return {@code true} if the user exists in the repository; {@code false} otherwise.
     */
    public boolean isUserExist(UUID userId) {
        return userRepository.isUserExist(userId); // Check user existence in the repository
    }
}
