package org.example.hci.auth;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

/**
 * Singleton class to manage MongoDB connection
 */
public class MongoDBConnector {
    private static MongoDBConnector instance;
    private MongoClient mongoClient;
    private MongoDatabase database;

    // Database configuration - adjust these values as needed
    private static final String CONNECTION_STRING = "MongoDB_URL";
    private static final String DATABASE_NAME = "furniture";

    // Collection names
    public static final String USERS_COLLECTION = "users";

    private MongoDBConnector() {
        try {
            // Create a MongoDB client
            mongoClient = MongoClients.create(CONNECTION_STRING);

            // Connect to the database
            database = mongoClient.getDatabase(DATABASE_NAME);

            System.out.println("MongoDB connection established successfully");
        } catch (Exception e) {
            System.err.println("Error connecting to MongoDB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Get the singleton instance
     */
    public static synchronized MongoDBConnector getInstance() {
        if (instance == null) {
            instance = new MongoDBConnector();
        }
        return instance;
    }

    /**
     * Get a collection from the database
     *
     * @param collectionName The name of the collection
     * @return The MongoDB collection
     */
    public MongoCollection<Document> getCollection(String collectionName) {
        return database.getCollection(collectionName);
    }

    /**
     * Close the MongoDB connection
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("MongoDB connection closed");
        }
    }
}