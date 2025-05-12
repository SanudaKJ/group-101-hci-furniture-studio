package org.example.hci.auth;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to handle user registration
 * In a real application, this would connect to a database
 */
public class RegistrationService {
    private Map<String, UserDetails> userDatabase;
    private ExtendedAuthenticationService authService;

    public RegistrationService() {
        // Initialize user database - in a real app, this would be a database connection
        userDatabase = new HashMap<>();

        // Reference to ExtendedAuthenticationService to update credentials when a new user registers
        authService = new ExtendedAuthenticationService();
    }

    /**
     * Register a new user
     *
     * @param username The username for the new account
     * @param password The password for the new account
     * @param email The email address for the new account
     * @param fullName The full name of the user
     * @return true if registration succeeds, false if username already exists
     */
    public boolean register(String username, String password, String email, String fullName) {
        // Check if the username already exists in the auth service
        if (isUsernameTaken(username)) {
            return false;
        }

        // Create new user details
        UserDetails newUser = new UserDetails(username, email, fullName);

        // In a real app, this would store to a database
        userDatabase.put(username, newUser);

        // Add to authentication service credentials
        // In a real app, this would properly hash the password
        addUserToAuthService(username, password);

        return true;
    }

    /**
     * Check if a username is already taken
     *
     * @param username The username to check
     * @return true if the username is taken, false otherwise
     */
    private boolean isUsernameTaken(String username) {
        // In a real app, we would check against the actual database

        // For demonstration, we'll check if authentication would work with a dummy password
        // If authentication fails with a known wrong password, the user likely doesn't exist
        // If it succeeds somehow, the username exists

        // First check our local database
        if (userDatabase.containsKey(username)) {
            return true;
        }

        // Then check the predefined users in AuthenticationService by trying a dummy auth
        // This is a bit of a hack but avoids reflection issues
        String knownPredefinedUsers[] = {"designer1", "designer2", "admin"};
        for (String predefined : knownPredefinedUsers) {
            if (username.equals(predefined)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Add a new user to the authentication service
     *
     * @param username The username
     * @param password The password
     */
    private void addUserToAuthService(String username, String password) {
        // In a real application, we would directly update the database
        // For this demo, we'll modify the AuthenticationService implementation

        // We'll create a custom method in our extended AuthenticationService class
        if (authService instanceof ExtendedAuthenticationService) {
            ((ExtendedAuthenticationService) authService).addUser(username, password);
        } else {
            // Store in our local database for now
            // The user will still be able to register but might have login issues
            // until the application is restarted
            System.out.println("Warning: Unable to directly update AuthenticationService.");
        }
    }

    /**
     * Inner class to store user details
     */
    private static class UserDetails {
        private String username;
        private String email;
        private String fullName;
        private String registrationDate;

        public UserDetails(String username, String email, String fullName) {
            this.username = username;
            this.email = email;
            this.fullName = fullName;
            this.registrationDate = java.time.LocalDate.now().toString();
        }

        // Getters and setters would go here
        public String getUsername() {
            return username;
        }

        public String getEmail() {
            return email;
        }

        public String getFullName() {
            return fullName;
        }

        public String getRegistrationDate() {
            return registrationDate;
        }
    }
}