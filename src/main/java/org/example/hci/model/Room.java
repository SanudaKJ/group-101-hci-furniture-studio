package org.example.hci.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Represents a room with dimensions and colors
 */
public class Room implements Serializable {
    private double width;
    private double length;
    private double height;
    private Color wallColor;
    private Color floorColor;

    public Room(double width, double length, double height) {
        this.width = width;
        this.length = length;
        this.height = height;
        this.wallColor = Color.WHITE;
        this.floorColor = new Color(210, 180, 140); // Light wood color
    }

    // Getters and setters
    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Color getWallColor() {
        return wallColor;
    }

    public void setWallColor(Color wallColor) {
        this.wallColor = wallColor;
    }

    public Color getFloorColor() {
        return floorColor;
    }

    public void setFloorColor(Color floorColor) {
        this.floorColor = floorColor;
    }
}