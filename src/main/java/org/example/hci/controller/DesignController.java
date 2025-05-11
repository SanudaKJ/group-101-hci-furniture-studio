package org.example.hci.controller;


import org.example.hci.model.Design;
import org.example.hci.model.FurnitureItem;
import org.example.hci.model.Room;

import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller for managing designs
 */
public class DesignController {
    private Design currentDesign;
    private Color currentFurnitureColor = new Color(165, 42, 42); // RGB values for brown
    private final String SAVE_DIRECTORY = "saved_designs/";

    public DesignController() {
        // Create save directory if it doesn't exist
        File saveDir = new File(SAVE_DIRECTORY);
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
    }

    /**
     * Gets the current active design
     *
     * @return The current design or null if none
     */
    public Design getCurrentDesign() {
        return currentDesign;
    }

    /**
     * Sets the current active design
     *
     * @param design The design to set as current
     */
    public void setCurrentDesign(Design design) {
        this.currentDesign = design;
    }

    /**
     * Get the current color for new furniture
     *
     * @return The current furniture color
     */
    public Color getCurrentFurnitureColor() {
        return currentFurnitureColor;
    }

    /**
     * Set the current color for new furniture
     *
     * @param color The color to use for new furniture
     */
    public void setCurrentFurnitureColor(Color color) {
        this.currentFurnitureColor = color;
    }

    /**
     * Add a furniture item to the current design
     *
     * @param item The item to add
     * @return true if added successfully, false otherwise
     */
    public boolean addFurnitureItem(FurnitureItem item) {
        if (currentDesign != null) {
            currentDesign.addFurnitureItem(item);
            return true;
        }
        return false;
    }

    /**
     * Remove a furniture item from the current design
     *
     * @param item The item to remove
     * @return true if removed successfully, false otherwise
     */
    public boolean removeFurnitureItem(FurnitureItem item) {
        if (currentDesign != null) {
            currentDesign.removeFurnitureItem(item);
            return true;
        }
        return false;
    }

    /**
     * Save the current design to file
     *
     * @param design The design to save
     * @return true if saved successfully, false otherwise
     */
    public boolean saveDesign(Design design) {
        if (design == null) {
            return false;
        }

        String filename = SAVE_DIRECTORY + design.getId() + ".ser";

        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(filename))) {
            oos.writeObject(design);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a design from storage
     *
     * @param design The design to delete
     * @return true if deleted successfully, false otherwise
     */
    public boolean deleteDesign(Design design) {
        if (design == null) {
            return false;
        }

        String filename = SAVE_DIRECTORY + design.getId() + ".ser";
        File file = new File(filename);

        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    /**
     * Load all saved designs from storage
     *
     * @return List of saved designs
     */
    public List<Design> getSavedDesigns() {
        List<Design> designs = new ArrayList<>();
        File saveDir = new File(SAVE_DIRECTORY);

        if (!saveDir.exists()) {
            return designs;
        }

        File[] files = saveDir.listFiles((dir, name) -> name.endsWith(".ser"));

        if (files == null) {
            return designs;
        }

        for (File file : files) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(file))) {
                Design design = (Design) ois.readObject();
                designs.add(design);
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        return designs;
    }

    /**
     * Create a new empty design
     *
     * @param name The name of the design
     * @param width The room width
     * @param length The room length
     * @param height The room height
     * @return The new design
     */
    public Design createNewDesign(String name, double width, double length, double height) {
        Room room = new Room(width, length, height);
        Design design = new Design(name, room);
        this.currentDesign = design;
        return design;
    }
}