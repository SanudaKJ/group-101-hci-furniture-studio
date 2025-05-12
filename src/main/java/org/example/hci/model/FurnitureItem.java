package org.example.hci.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.UUID;

/**
 * Represents a furniture item in a design
 */
public class FurnitureItem implements Serializable {
    public enum FurnitureType {
        CHAIR, TABLE, SOFA, BED, CABINET
    }

    private String id;
    private FurnitureType type;
    private double x;
    private double y;
    private double width;
    private double depth;
    private double height;
    private Color color;
    private double rotationAngle; // in degrees

    public FurnitureItem(FurnitureType type, double x, double y) {
        this.id = UUID.randomUUID().toString();
        this.type = type;
        this.x = x;
        this.y = y;
        this.rotationAngle = 0;

        // Set default dimensions based on type
        switch (type) {
            case CHAIR:
                this.width = 0.5;
                this.depth = 0.5;
                this.height = 0.9;
                this.color = new Color(139, 69, 19); // Brown
                break;
            case TABLE:
                this.width = 1.2;
                this.depth = 0.8;
                this.height = 0.75;
                this.color = new Color(101, 67, 33); // Dark brown
                break;
            case SOFA:
                this.width = 2.0;
                this.depth = 0.9;
                this.height = 0.8;
                this.color = new Color(72, 61, 139); // Dark slate blue
                break;
            case BED:
                this.width = 1.6;
                this.depth = 2.0;
                this.height = 0.5;
                this.color = new Color(255, 250, 240); // Floral white
                break;
            case CABINET:
                this.width = 1.0;
                this.depth = 0.5;
                this.height = 1.8;
                this.color = new Color(222, 184, 135); // Burlywood
                break;
        }
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public FurnitureType getType() {
        return type;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public double getRotationAngle() {
        return rotationAngle;
    }

    public void setRotationAngle(double rotationAngle) {
        this.rotationAngle = rotationAngle;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        FurnitureItem item = (FurnitureItem) obj;
        return id.equals(item.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}