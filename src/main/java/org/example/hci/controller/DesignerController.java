package org.example.hci.controller;


import org.example.hci.model.Design;
import org.example.hci.model.FurnitureItem;
import org.example.hci.model.Room;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing designer actions
 */
public class DesignerController {
    private DesignController designController;

    public DesignerController() {
        this.designController = new DesignController();
    }

    /**
     * Create a new design
     *
     * @param name The name of the design
     * @param roomWidth The room width
     * @param roomLength The room length
     * @param roomHeight The room height
     * @return The created design
     */
    public Design createNewDesign(String name, double roomWidth, double roomLength, double roomHeight) {
        return designController.createNewDesign(name, roomWidth, roomLength, roomHeight);
    }

    /**
     * Get the current design
     *
     * @return The current design
     */
    public Design getCurrentDesign() {
        return designController.getCurrentDesign();
    }

    /**
     * Save the current design
     *
     * @return true if saved successfully, false otherwise
     */
    public boolean saveCurrentDesign() {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            return designController.saveDesign(currentDesign);
        }
        return false;
    }

    /**
     * Open a design
     *
     * @param design The design to open
     */
    public void openDesign(Design design) {
        designController.setCurrentDesign(design);
    }

    /**
     * Get all saved designs
     *
     * @return List of saved designs
     */
    public List<Design> getSavedDesigns() {
        return designController.getSavedDesigns();
    }

    /**
     * Delete a design
     *
     * @param design The design to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDesign(Design design) {
        return designController.deleteDesign(design);
    }

    /**
     * Add a furniture item to the current design
     *
     * @param furnitureType The type of furniture
     * @param x The x position
     * @param y The y position
     * @return The added furniture item, or null if failed
     */
    public FurnitureItem addFurnitureItem(FurnitureItem.FurnitureType furnitureType, double x, double y) {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            FurnitureItem item = new FurnitureItem(furnitureType, x, y);
            item.setColor(designController.getCurrentFurnitureColor());
            currentDesign.addFurnitureItem(item);
            return item;
        }
        return null;
    }

    /**
     * Remove a furniture item from the current design
     *
     * @param item The item to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeFurnitureItem(FurnitureItem item) {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            currentDesign.removeFurnitureItem(item);
            return true;
        }
        return false;
    }

    /**
     * Set the color of a furniture item
     *
     * @param item The item to color
     * @param color The color to set
     */
    public void setFurnitureColor(FurnitureItem item, Color color) {
        if (item != null) {
            item.setColor(color);
        }
    }

    /**
     * Set the current furniture color
     *
     * @param color The color to set
     */
    public void setCurrentFurnitureColor(Color color) {
        designController.setCurrentFurnitureColor(color);
    }

    /**
     * Set room dimensions
     *
     * @param width The room width
     * @param length The room length
     * @param height The room height
     * @return true if set successfully, false otherwise
     */
    public boolean setRoomDimensions(double width, double length, double height) {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            Room room = currentDesign.getRoom();
            room.setWidth(width);
            room.setLength(length);
            room.setHeight(height);
            return true;
        }
        return false;
    }

    /**
     * Set room colors
     *
     * @param wallColor The wall color
     * @param floorColor The floor color
     * @return true if set successfully, false otherwise
     */
    public boolean setRoomColors(Color wallColor, Color floorColor) {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            Room room = currentDesign.getRoom();
            if (wallColor != null) {
                room.setWallColor(wallColor);
            }
            if (floorColor != null) {
                room.setFloorColor(floorColor);
            }
            return true;
        }
        return false;
    }
}