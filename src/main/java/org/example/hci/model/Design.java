package org.example.hci.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents a furniture design with a room and furniture items
 */
public class Design implements Serializable {
    private String id;
    private String name;
    private Room room;
    private List<FurnitureItem> furnitureItems;
    private long createdTimestamp;
    private long lastModifiedTimestamp;

    public Design(String name, Room room) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.room = room;
        this.furnitureItems = new ArrayList<>();
        this.createdTimestamp = System.currentTimeMillis();
        this.lastModifiedTimestamp = this.createdTimestamp;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        updateLastModified();
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
        updateLastModified();
    }

    public List<FurnitureItem> getFurnitureItems() {
        return furnitureItems;
    }

    public void addFurnitureItem(FurnitureItem item) {
        furnitureItems.add(item);
        updateLastModified();
    }

    public void removeFurnitureItem(FurnitureItem item) {
        furnitureItems.remove(item);
        updateLastModified();
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    private void updateLastModified() {
        this.lastModifiedTimestamp = System.currentTimeMillis();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Design design = (Design) obj;
        return id.equals(design.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}