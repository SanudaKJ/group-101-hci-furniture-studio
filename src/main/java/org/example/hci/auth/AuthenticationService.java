package org.example.hci.auth;
import java.util.HashMap;
import java.util.Map;

/**
 * Service to handle user authentication
 * In a real application, this would connect to a database
 */
public class AuthenticationService {
    private Map<String, String> userCredentials;

    public AuthenticationService() {
        // Mock user data - in a real app, this would come from a database
        userCredentials = new HashMap<>();
        userCredentials.put("designer1", "password1");
        userCredentials.put("designer2", "password2");
        userCredentials.put("admin", "admin123");
    }

    /**
     * Authenticate a user based on username and password
     *
     * @param username The username
     * @param password The password
     * @return true if authentication succeeds, false otherwise
     */
    public boolean authenticate(String username, String password) {
        if (username == null || password == null) {
            return false;
        }

        String storedPassword = userCredentials.get(username);
        return storedPassword != null && storedPassword.equals(password);
    }
}