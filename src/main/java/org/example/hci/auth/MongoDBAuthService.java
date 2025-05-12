package org.example.hci.auth;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.hci.auth.MongoDBConnector;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;

/**
 * Authentication service using MongoDB for storage
 */
public class MongoDBAuthService {
    private MongoCollection<Document> usersCollection;

    public MongoDBAuthService() {
        // Get the MongoDB collection for users
        usersCollection = MongoDBConnector.getInstance().getCollection(MongoDBConnector.USERS_COLLECTION);

        // Initialize default users if the collection is empty
        initializeDefaultUsers();
    }

    /**
     * Initialize default users in the database if they don't exist
     */
    private void initializeDefaultUsers() {
        // Check if we have any users
        if (usersCollection.countDocuments() == 0) {
            System.out.println("Initializing default users in MongoDB...");

            // Create default users
            registerUser("designer1", "password1", "designer1@example.com", "Default Designer 1");
            registerUser("designer2", "password2", "designer2@example.com", "Default Designer 2");
            registerUser("admin", "admin123", "admin@example.com", "Administrator");

            System.out.println("Default users created successfully");
        }
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

        try {
            // Find the user document
            Document userDoc = usersCollection.find(Filters.eq("username", username)).first();

            if (userDoc != null) {
                String storedHashedPassword = userDoc.getString("passwordHash");
                String salt = userDoc.getString("salt");

                // Hash the provided password with the stored salt
                String hashedPassword = hashPassword(password, salt);

                // Compare the hashed password with the stored hash
                return storedHashedPassword.equals(hashedPassword);
            }
        } catch (Exception e) {
            System.err.println("Error during authentication: " + e.getMessage());
            e.printStackTrace();
        }

        return false;
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
    public boolean registerUser(String username, String password, String email, String fullName) {
        try {
            // Check if username already exists
            if (isUsernameTaken(username)) {
                System.out.println("Username already taken: " + username);
                return false;
            }

            // Generate a random salt
            String salt = generateSalt();

            // Hash the password with the salt
            String hashedPassword = hashPassword(password, salt);

            // Create a new user document
            Document userDoc = new Document()
                    .append("username", username)
                    .append("passwordHash", hashedPassword)
                    .append("salt", salt)
                    .append("email", email)
                    .append("fullName", fullName)
                    .append("registrationDate", new Date())
                    .append("lastLogin", null)
                    .append("isActive", true);

            // Insert the document into the collection
            InsertOneResult result = usersCollection.insertOne(userDoc);

            System.out.println("User registered successfully: " + username);
            return result.wasAcknowledged();

        } catch (Exception e) {
            System.err.println("Error during registration: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Check if a username is already taken
     *
     * @param username The username to check
     * @return true if the username is taken, false otherwise
     */
    public boolean isUsernameTaken(String username) {
        Document userDoc = usersCollection.find(Filters.eq("username", username)).first();
        return userDoc != null;
    }

    /**
     * Update the last login timestamp for a user
     *
     * @param username The username
     */
    public void updateLastLogin(String username) {
        Bson filter = Filters.eq("username", username);
        Bson update = Updates.set("lastLogin", new Date());
        usersCollection.updateOne(filter, update);
    }

    /**
     * Generate a random salt for password hashing
     *
     * @return A Base64 encoded random salt
     */
    private String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * Hash a password with a salt using SHA-256
     *
     * @param password The password to hash
     * @param salt The salt to use
     * @return The hashed password
     */
    private String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}