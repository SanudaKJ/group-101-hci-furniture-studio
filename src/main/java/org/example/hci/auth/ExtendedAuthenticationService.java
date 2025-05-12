package org.example.hci.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Extended authentication service that allows adding new users
 * Extends the basic AuthenticationService with additional functionality
 */
public class ExtendedAuthenticationService extends AuthenticationService {
    private Map<String, String> additionalUsers;

    public ExtendedAuthenticationService() {
        super(); // Initialize the parent class
        additionalUsers = new HashMap<>();
    }

    /**
     * Add a new user to the authentication system
     *
     * @param username The username for the new user
     * @param password The password for the new user
     * @return true if the user was added successfully, false if the username already exists
     */
    public boolean addUser(String username, String password) {
        // Store the new credentials in our additional map
        additionalUsers.put(username, password);
        return true;
    }

    /**
     * Authenticate a user based on username and password
     * Overrides the parent method to also check additionalUsers
     *
     * @param username The username
     * @param password The password
     * @return true if authentication succeeds, false otherwise
     */
    @Override
    public boolean authenticate(String username, String password) {
        // First check the parent authentication method
        if (super.authenticate(username, password)) {
            return true;
        }

        // If not found in parent, check our additional users
        if (username == null || password == null) {
            return false;
        }

        String storedPassword = additionalUsers.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}