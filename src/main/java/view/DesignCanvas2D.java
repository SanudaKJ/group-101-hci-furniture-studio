package org.example.hci.view;

import org.example.hci.model.Design;
import org.example.hci.model.FurnitureItem;
import org.example.hci.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * Canvas for 2D visualization and editing of furniture design
 */
public class DesignCanvas2D extends JPanel implements MouseListener, MouseMotionListener {
    private Design design;
    private FurnitureItem selectedItem;
    private FurnitureItem draggingItem;
    private Point dragStart;
    private double scale = 50.0; // pixels per meter
    private int panX = 0;
    private int panY = 0;

    private FurnitureItem pendingFurnitureItem = null;
    private FurnitureItem.FurnitureType pendingFurnitureType = null;

    public DesignCanvas2D() {
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createLoweredBevelBorder());
        addMouseListener(this);
        addMouseMotionListener(this);

        // Add keyboard shortcuts for operations
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });
    }

    /**
     * Set the design to visualize
     *
     * @param design The design to visualize
     */
    public void setDesign(Design design) {
        this.design = design;
        repaint();
    }
    public void setPendingFurniture(FurnitureItem.FurnitureType type) {
        this.pendingFurnitureType = type;
        this.pendingFurnitureItem = null;
        setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Apply panning
        g2d.translate(panX, panY);

        // Draw the room if design is set
        if (design != null) {
            Room room = design.getRoom();

            // Draw room outline
            int roomWidth = (int) (room.getWidth() * scale);
            int roomLength = (int) (room.getLength() * scale);

            // Center the room in the canvas
            int centerX = (getWidth() - roomWidth) / 2;
            int centerY = (getHeight() - roomLength) / 2;

            // Draw floor
            g2d.setColor(room.getFloorColor());
            g2d.fillRect(centerX, centerY, roomWidth, roomLength);

            // Draw walls
            g2d.setColor(room.getWallColor());
            g2d.setStroke(new BasicStroke(4.0f));
            g2d.drawRect(centerX, centerY, roomWidth, roomLength);

            // Draw furniture items
            for (FurnitureItem item : design.getFurnitureItems()) {
                drawFurnitureItem(g2d, item, centerX, centerY);
            }

            // Draw room dimensions
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 12));

            // Width dimension
            String widthText = String.format("%.2f m", room.getWidth());
            g2d.drawString(widthText, centerX + roomWidth / 2 - 20, centerY - 10);

            // Length dimension
            String lengthText = String.format("%.2f m", room.getLength());
            g2d.drawString(lengthText, centerX - 30, centerY + roomLength / 2);
        } else {
            // Draw a message if no design is set
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String message = "No design loaded. Create or open a design.";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            g2d.drawString(message, (getWidth() - textWidth) / 2, getHeight() / 2);
        }


        g2d.dispose();
    }

    /**
     * Draw a furniture item on the canvas
     *
     * @param g2d The graphics context
     * @param item The furniture item to draw
     * @param roomX The x position of the room
     * @param roomY The y position of the room
     */
    private void drawFurnitureItem(Graphics2D g2d, FurnitureItem item, int roomX, int roomY) {
        int x = roomX + (int) (item.getX() * scale);
        int y = roomY + (int) (item.getY() * scale);
        int width = (int) (item.getWidth() * scale);
        int depth = (int) (item.getDepth() * scale);

        // Save the current transform
        AffineTransform oldTransform = g2d.getTransform();

        // Apply rotation if needed
        if (item.getRotationAngle() != 0) {
            g2d.rotate(Math.toRadians(item.getRotationAngle()), x + width / 2, y + depth / 2);
        }

        // Draw the furniture item
        g2d.setColor(item.getColor());
        g2d.fillRect(x, y, width, depth);

        // Draw outline, thicker if selected
        if (item == selectedItem) {
            g2d.setStroke(new BasicStroke(3.0f));
            g2d.setColor(Color.BLUE);
        } else {
            g2d.setStroke(new BasicStroke(1.0f));
            g2d.setColor(Color.BLACK);
        }
        g2d.drawRect(x, y, width, depth);

        // Draw type indicator
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Arial", Font.PLAIN, 10));
        g2d.drawString(item.getType().toString(), x + 5, y + 15);

        // Restore the original transform
        g2d.setTransform(oldTransform);
    }

    /**
     * Get the furniture item at the given point
     *
     * @param x The x coordinate
     * @param y The y coordinate
     * @return The furniture item at the point, or null if none
     */
    private FurnitureItem getFurnitureItemAt(int x, int y) {
        if (design == null) {
            return null;
        }

        Room room = design.getRoom();
        int roomWidth = (int) (room.getWidth() * scale);
        int roomLength = (int) (room.getLength() * scale);
        int roomX = (getWidth() - roomWidth) / 2 + panX;
        int roomY = (getHeight() - roomLength) / 2 + panY;

        // Check in reverse order to select items on top first
        for (int i = design.getFurnitureItems().size() - 1; i >= 0; i--) {
            FurnitureItem item = design.getFurnitureItems().get(i);

            int itemX = roomX + (int) (item.getX() * scale);
            int itemY = roomY + (int) (item.getY() * scale);
            int itemWidth = (int) (item.getWidth() * scale);
            int itemDepth = (int) (item.getDepth() * scale);

            // Create a rectangle for the item
            Rectangle2D rect = new Rectangle2D.Double(itemX, itemY, itemWidth, itemDepth);

            // If the item is rotated, we need a more complex check (for simplicity, just check bounding box)
            if (rect.contains(x, y)) {
                return item;
            }
        }

        return null;
    }

    /**
     * Handle keyboard shortcuts
     *
     * @param e The key event
     */
    private void handleKeyPress(KeyEvent e) {
        if (selectedItem != null) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_DELETE:
                    // Delete selected item
                    design.removeFurnitureItem(selectedItem);
                    selectedItem = null;
                    repaint();
                    break;
                case KeyEvent.VK_R:
                    // Rotate selected item by 45 degrees
                    selectedItem.setRotationAngle(
                            (selectedItem.getRotationAngle() + 45) % 360);
                    repaint();
                    break;
                case KeyEvent.VK_UP:
                    // Move up
                    if (selectedItem.getY() > 0.1) {
                        selectedItem.setY(selectedItem.getY() - 0.1);
                        repaint();
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    // Move down
                    if (selectedItem.getY() < design.getRoom().getLength() - selectedItem.getDepth()) {
                        selectedItem.setY(selectedItem.getY() + 0.1);
                        repaint();
                    }
                    break;
                case KeyEvent.VK_LEFT:
                    // Move left
                    if (selectedItem.getX() > 0.1) {
                        selectedItem.setX(selectedItem.getX() - 0.1);
                        repaint();
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    // Move right
                    if (selectedItem.getX() < design.getRoom().getWidth() - selectedItem.getWidth()) {
                        selectedItem.setX(selectedItem.getX() + 0.1);
                        repaint();
                    }
                    break;
            }
        }

        // Pan with arrow keys when holding Shift
        if (e.isShiftDown()) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    panY += 10;
                    repaint();
                    break;
                case KeyEvent.VK_DOWN:
                    panY -= 10;
                    repaint();
                    break;
                case KeyEvent.VK_LEFT:
                    panX += 10;
                    repaint();
                    break;
                case KeyEvent.VK_RIGHT:
                    panX -= 10;
                    repaint();
                    break;
            }
        }
    }

    // Mouse listener methods
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();

        if (design != null) {
            if (pendingFurnitureType != null) {
                // Place new furniture at click position
                Room room = design.getRoom();
                int roomWidth = (int) (room.getWidth() * scale);
                int roomLength = (int) (room.getLength() * scale);
                int roomX = (getWidth() - roomWidth) / 2 + panX;
                int roomY = (getHeight() - roomLength) / 2 + panY;

                // Convert screen coordinates to room coordinates
                double itemX = (e.getX() - roomX) / scale;
                double itemY = (e.getY() - roomY) / scale;

                // Check if click is within room boundaries
                if (itemX >= 0 && itemX <= room.getWidth() &&
                        itemY >= 0 && itemY <= room.getLength()) {

                    // Create and add the furniture item
                    FurnitureItem item = new FurnitureItem(pendingFurnitureType, itemX, itemY);
                    design.addFurnitureItem(item);

                    // Reset pending furniture
                    pendingFurnitureType = null;
                    setCursor(Cursor.getDefaultCursor());

                    repaint();
                }
            } else {
                // Handle selection as before
                FurnitureItem item = getFurnitureItemAt(e.getX(), e.getY());

                if (item != null) {
                    selectedItem = item;
                } else {
                    selectedItem = null;
                }

                repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();

        if (design != null) {
            FurnitureItem item = getFurnitureItemAt(e.getX(), e.getY());

            if (item != null) {
                draggingItem = item;
                dragStart = e.getPoint();
            } else {
                // Start panning if not on an item
                dragStart = e.getPoint();
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        draggingItem = null;
        dragStart = null;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Request focus when mouse enters to enable keyboard shortcuts
        requestFocusInWindow();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Not used
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (dragStart != null) {
            if (draggingItem != null) {
                // Move the selected furniture item
                Room room = design.getRoom();
                int roomWidth = (int) (room.getWidth() * scale);
                int roomLength = (int) (room.getLength() * scale);
                int roomX = (getWidth() - roomWidth) / 2 + panX;
                int roomY = (getHeight() - roomLength) / 2 + panY;

                // Calculate the new position
                double newX = draggingItem.getX() + (e.getX() - dragStart.x) / scale;
                double newY = draggingItem.getY() + (e.getY() - dragStart.y) / scale;

                // Constrain to room boundaries
                newX = Math.max(0, Math.min(newX, room.getWidth() - draggingItem.getWidth()));
                newY = Math.max(0, Math.min(newY, room.getLength() - draggingItem.getDepth()));

                // Update item position
                draggingItem.setX(newX);
                draggingItem.setY(newY);

                dragStart = e.getPoint();
                repaint();
            } else {
                // Pan the view
                int dx = e.getX() - dragStart.x;
                int dy = e.getY() - dragStart.y;

                panX += dx;
                panY += dy;

                dragStart = e.getPoint();
                repaint();
            }
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Change cursor when over a furniture item
        if (design != null) {
            FurnitureItem item = getFurnitureItemAt(e.getX(), e.getY());

            if (item != null) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            } else {
                setCursor(Cursor.getDefaultCursor());
            }
        }
    }
}