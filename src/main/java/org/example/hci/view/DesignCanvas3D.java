package org.example.hci.view;


import org.example.hci.model.Design;
import org.example.hci.model.FurnitureItem;
import org.example.hci.model.Room;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

/**
 * Canvas for 3D visualization of furniture design
 * This is a simple implementation that uses 2D graphics to create a pseudo-3D view.
 * In a real application, you might use a proper 3D library like JavaFX or JOGL.
 */
public class DesignCanvas3D extends JPanel implements MouseListener, MouseMotionListener {
    private Design design;
    private double viewAngle = 45.0; // Viewing angle in degrees
    private double viewElevation = 30.0; // Elevation angle in degrees
    private double scale = 40.0; // pixels per meter
    private Point dragStart;
    private boolean isDragging = false;

    // Drawing constants
    private static final double WALL_HEIGHT_SCALE = 0.7; // Make walls shorter for better visibility

    public DesignCanvas3D() {
        setBackground(new Color(230, 230, 250)); // Light lavender background
        setBorder(BorderFactory.createLoweredBevelBorder());
        addMouseListener(this);
        addMouseMotionListener(this);

        // Add keyboard shortcuts for view manipulation
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the design if set
        if (design != null) {
            render3DView(g2d);
        } else {
            // Draw a message if no design is set
            g2d.setColor(Color.GRAY);
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            String message = "No design loaded. Create or open a design.";
            FontMetrics fm = g2d.getFontMetrics();
            int textWidth = fm.stringWidth(message);
            g2d.drawString(message, (getWidth() - textWidth) / 2, getHeight() / 2);
        }

        // Draw view controls help
        g2d.setColor(Color.DARK_GRAY);
        g2d.setFont(new Font("Arial", Font.PLAIN, 12));
        g2d.drawString("Drag to rotate view | +/- to zoom | Arrow keys to adjust elevation", 10, getHeight() - 10);

        g2d.dispose();
    }

    /**
     * Render the 3D view of the design
     *
     * @param g2d The graphics context
     */
    private void render3DView(Graphics2D g2d) {
        Room room = design.getRoom();

        // Calculate the dimensions in pixels
        int roomWidth = (int) (room.getWidth() * scale);
        int roomLength = (int) (room.getLength() * scale);
        int roomHeight = (int) (room.getHeight() * scale * WALL_HEIGHT_SCALE);

        // Center of the room in screen coordinates
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;

        // Calculate the isometric projection factors
        double angleRad = Math.toRadians(viewAngle);
        double elevationRad = Math.toRadians(viewElevation);
        double cosAngle = Math.cos(angleRad);
        double sinAngle = Math.sin(angleRad);
        double elevationFactor = Math.sin(elevationRad);

        // Calculate the corner points of the room in 3D space
        int[][] floorPoints = new int[4][2];
        int[][] ceilingPoints = new int[4][2];

        // Floor corners (clockwise from bottom-left)
        floorPoints[0] = project3DTo2D(-roomWidth/2, -roomLength/2, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        floorPoints[1] = project3DTo2D(roomWidth/2, -roomLength/2, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        floorPoints[2] = project3DTo2D(roomWidth/2, roomLength/2, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        floorPoints[3] = project3DTo2D(-roomWidth/2, roomLength/2, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);

        // Ceiling corners (same order as floor, but at height)
        ceilingPoints[0] = project3DTo2D(-roomWidth/2, -roomLength/2, roomHeight, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        ceilingPoints[1] = project3DTo2D(roomWidth/2, -roomLength/2, roomHeight, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        ceilingPoints[2] = project3DTo2D(roomWidth/2, roomLength/2, roomHeight, centerX, centerY, cosAngle, sinAngle, elevationFactor);
        ceilingPoints[3] = project3DTo2D(-roomWidth/2, roomLength/2, roomHeight, centerX, centerY, cosAngle, sinAngle, elevationFactor);

        // Create polygon for floor
        Polygon floorPoly = new Polygon();
        for (int[] point : floorPoints) {
            floorPoly.addPoint(point[0], point[1]);
        }

        // Create polygon for ceiling
        Polygon ceilingPoly = new Polygon();
        for (int[] point : ceilingPoints) {
            ceilingPoly.addPoint(point[0], point[1]);
        }

        // Determine which walls to draw based on viewing angle
        boolean drawLeftWall = sinAngle > 0;
        boolean drawRightWall = cosAngle < 0;
        boolean drawBackWall = true; // Always draw back wall

        // Draw floor
        g2d.setColor(room.getFloorColor());
        g2d.fillPolygon(floorPoly);

        // Draw walls
        g2d.setColor(room.getWallColor());

        // Left wall
        if (drawLeftWall) {
            Polygon leftWall = new Polygon();
            leftWall.addPoint(floorPoints[0][0], floorPoints[0][1]);
            leftWall.addPoint(floorPoints[3][0], floorPoints[3][1]);
            leftWall.addPoint(ceilingPoints[3][0], ceilingPoints[3][1]);
            leftWall.addPoint(ceilingPoints[0][0], ceilingPoints[0][1]);
            g2d.fillPolygon(leftWall);
        }

        // Right wall
        if (drawRightWall) {
            Polygon rightWall = new Polygon();
            rightWall.addPoint(floorPoints[1][0], floorPoints[1][1]);
            rightWall.addPoint(floorPoints[2][0], floorPoints[2][1]);
            rightWall.addPoint(ceilingPoints[2][0], ceilingPoints[2][1]);
            rightWall.addPoint(ceilingPoints[1][0], ceilingPoints[1][1]);
            g2d.fillPolygon(rightWall);
        }

        // Back wall
        if (drawBackWall) {
            Polygon backWall = new Polygon();
            backWall.addPoint(floorPoints[0][0], floorPoints[0][1]);
            backWall.addPoint(floorPoints[1][0], floorPoints[1][1]);
            backWall.addPoint(ceilingPoints[1][0], ceilingPoints[1][1]);
            backWall.addPoint(ceilingPoints[0][0], ceilingPoints[0][1]);
            g2d.fillPolygon(backWall);
        }

        // Draw furniture items
        drawFurnitureItems(g2d, centerX, centerY, cosAngle, sinAngle, elevationFactor);

        // Draw outlines
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(1.0f));

        // Floor outline
        g2d.drawPolygon(floorPoly);

        // Wall outlines
        if (drawLeftWall) {
            g2d.drawLine(floorPoints[0][0], floorPoints[0][1], floorPoints[3][0], floorPoints[3][1]);
            g2d.drawLine(floorPoints[3][0], floorPoints[3][1], ceilingPoints[3][0], ceilingPoints[3][1]);
            g2d.drawLine(ceilingPoints[3][0], ceilingPoints[3][1], ceilingPoints[0][0], ceilingPoints[0][1]);
            g2d.drawLine(ceilingPoints[0][0], ceilingPoints[0][1], floorPoints[0][0], floorPoints[0][1]);
        }

        if (drawRightWall) {
            g2d.drawLine(floorPoints[1][0], floorPoints[1][1], floorPoints[2][0], floorPoints[2][1]);
            g2d.drawLine(floorPoints[2][0], floorPoints[2][1], ceilingPoints[2][0], ceilingPoints[2][1]);
            g2d.drawLine(ceilingPoints[2][0], ceilingPoints[2][1], ceilingPoints[1][0], ceilingPoints[1][1]);
            g2d.drawLine(ceilingPoints[1][0], ceilingPoints[1][1], floorPoints[1][0], floorPoints[1][1]);
        }

        if (drawBackWall) {
            g2d.drawLine(floorPoints[0][0], floorPoints[0][1], floorPoints[1][0], floorPoints[1][1]);
            g2d.drawLine(floorPoints[1][0], floorPoints[1][1], ceilingPoints[1][0], ceilingPoints[1][1]);
            g2d.drawLine(ceilingPoints[1][0], ceilingPoints[1][1], ceilingPoints[0][0], ceilingPoints[0][1]);
            g2d.drawLine(ceilingPoints[0][0], ceilingPoints[0][1], floorPoints[0][0], floorPoints[0][1]);
        }
    }

    /**
     * Draw furniture items in 3D
     *
     * @param g2d The graphics context
     * @param centerX Center X coordinate
     * @param centerY Center Y coordinate
     * @param cosAngle Cosine of viewing angle
     * @param sinAngle Sine of viewing angle
     * @param elevationFactor Elevation factor
     */
    private void drawFurnitureItems(Graphics2D g2d, int centerX, int centerY,
                                    double cosAngle, double sinAngle, double elevationFactor) {
        if (design == null) {
            return;
        }

        // Sort furniture items by distance from viewer for proper depth
        // This is a simple approach - back to front rendering
        design.getFurnitureItems().sort((item1, item2) -> {
            double z1 = item1.getX() * cosAngle + item1.getY() * sinAngle;
            double z2 = item2.getX() * cosAngle + item2.getY() * sinAngle;
            return Double.compare(z2, z1); // Sort front to back
        });

        Room room = design.getRoom();
        double roomWidth = room.getWidth();
        double roomLength = room.getLength();

        // Draw each furniture item
        for (FurnitureItem item : design.getFurnitureItems()) {
            // Convert furniture position from room coordinates to centered coordinates
            double x = item.getX() - roomWidth / 2;
            double y = item.getY() - roomLength / 2;
            double width = item.getWidth();
            double depth = item.getDepth();
            double height = item.getHeight();

            // Apply rotation if needed
            if (item.getRotationAngle() != 0) {
                double rotRad = Math.toRadians(item.getRotationAngle());
                double cosRot = Math.cos(rotRad);
                double sinRot = Math.sin(rotRad);

                // Rotate around center
                double centerX3D = x + width / 2;
                double centerY3D = y + depth / 2;

                // Transformed coordinates
                x = centerX3D - width / 2 * cosRot + depth / 2 * sinRot;
                y = centerY3D - width / 2 * sinRot - depth / 2 * cosRot;

                // Swap width and depth if rotation is close to 90 or 270 degrees
                if (Math.abs(Math.sin(rotRad)) > 0.7) {
                    double temp = width;
                    width = depth;
                    depth = temp;
                }
            }

            // Calculate the corner points in 3D space
            int[][] bottomPoints = new int[4][2];
            int[][] topPoints = new int[4][2];

            // Scale to pixels
            x *= scale;
            y *= scale;
            width *= scale;
            depth *= scale;
            height *= scale;

            // Calculate bottom corners (clockwise from bottom-left)
            bottomPoints[0] = project3DTo2D(x, y, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            bottomPoints[1] = project3DTo2D(x + width, y, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            bottomPoints[2] = project3DTo2D(x + width, y + depth, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            bottomPoints[3] = project3DTo2D(x, y + depth, 0, centerX, centerY, cosAngle, sinAngle, elevationFactor);

            // Calculate top corners (same order)
            topPoints[0] = project3DTo2D(x, y, height, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            topPoints[1] = project3DTo2D(x + width, y, height, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            topPoints[2] = project3DTo2D(x + width, y + depth, height, centerX, centerY, cosAngle, sinAngle, elevationFactor);
            topPoints[3] = project3DTo2D(x, y + depth, height, centerX, centerY, cosAngle, sinAngle, elevationFactor);

            // Create polygons for each face
            Polygon bottomFace = new Polygon();
            Polygon topFace = new Polygon();
            Polygon frontFace = new Polygon();
            Polygon backFace = new Polygon();
            Polygon leftFace = new Polygon();
            Polygon rightFace = new Polygon();

            // Add points to faces
            for (int i = 0; i < 4; i++) {
                bottomFace.addPoint(bottomPoints[i][0], bottomPoints[i][1]);
                topFace.addPoint(topPoints[i][0], topPoints[i][1]);
            }

            // Front face
            frontFace.addPoint(bottomPoints[0][0], bottomPoints[0][1]);
            frontFace.addPoint(bottomPoints[1][0], bottomPoints[1][1]);
            frontFace.addPoint(topPoints[1][0], topPoints[1][1]);
            frontFace.addPoint(topPoints[0][0], topPoints[0][1]);

            // Back face
            backFace.addPoint(bottomPoints[2][0], bottomPoints[2][1]);
            backFace.addPoint(bottomPoints[3][0], bottomPoints[3][1]);
            backFace.addPoint(topPoints[3][0], topPoints[3][1]);
            backFace.addPoint(topPoints[2][0], topPoints[2][1]);

            // Left face
            leftFace.addPoint(bottomPoints[0][0], bottomPoints[0][1]);
            leftFace.addPoint(bottomPoints[3][0], bottomPoints[3][1]);
            leftFace.addPoint(topPoints[3][0], topPoints[3][1]);
            leftFace.addPoint(topPoints[0][0], topPoints[0][1]);

            // Right face
            rightFace.addPoint(bottomPoints[1][0], bottomPoints[1][1]);
            rightFace.addPoint(bottomPoints[2][0], bottomPoints[2][1]);
            rightFace.addPoint(topPoints[2][0], topPoints[2][1]);
            rightFace.addPoint(topPoints[1][0], topPoints[1][1]);

            // Determine which faces to draw based on viewing angle
            boolean drawLeftFace = sinAngle > 0;
            boolean drawRightFace = cosAngle < 0;
            boolean drawFrontFace = true; // Always draw front face
            boolean drawBackFace = false; // Don't draw back face (usually hidden)

            // Fill faces with shaded colors
            Color baseColor = item.getColor();
            Color topColor = baseColor;
            Color frontColor = darken(baseColor, 0.8);
            Color leftColor = darken(baseColor, 0.6);
            Color rightColor = darken(baseColor, 0.4);

            // Bottom face is usually not visible
            g2d.setColor(topColor);
            g2d.fillPolygon(topFace);

            if (drawFrontFace) {
                g2d.setColor(frontColor);
                g2d.fillPolygon(frontFace);
            }

            if (drawBackFace) {
                g2d.setColor(frontColor);
                g2d.fillPolygon(backFace);
            }

            if (drawLeftFace) {
                g2d.setColor(leftColor);
                g2d.fillPolygon(leftFace);
            }

            if (drawRightFace) {
                g2d.setColor(rightColor);
                g2d.fillPolygon(rightFace);
            }

            // Draw outlines
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(1.0f));

            // Top face outline
            g2d.drawPolygon(topFace);

            // Vertical edges
            for (int i = 0; i < 4; i++) {
                g2d.drawLine(bottomPoints[i][0], bottomPoints[i][1], topPoints[i][0], topPoints[i][1]);
            }

            // Front face outline
            if (drawFrontFace) {
                g2d.drawPolygon(frontFace);
            }

            // Back face outline
            if (drawBackFace) {
                g2d.drawPolygon(backFace);
            }

            // Left face outline
            if (drawLeftFace) {
                g2d.drawPolygon(leftFace);
            }

            // Right face outline
            if (drawRightFace) {
                g2d.drawPolygon(rightFace);
            }

            // Add a label for the furniture type
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 10));
            int labelX = (topPoints[0][0] + topPoints[1][0] + topPoints[2][0] + topPoints[3][0]) / 4;
            int labelY = (topPoints[0][1] + topPoints[1][1] + topPoints[2][1] + topPoints[3][1]) / 4;
            g2d.drawString(item.getType().toString(), labelX, labelY);
        }
    }

    /**
     * Project a 3D point to 2D screen coordinates
     *
     * @param x3d X coordinate in 3D space
     * @param y3d Y coordinate in 3D space
     * @param z3d Z coordinate in 3D space
     * @param centerX Center X coordinate on screen
     * @param centerY Center Y coordinate on screen
     * @param cosAngle Cosine of viewing angle
     * @param sinAngle Sine of viewing angle
     * @param elevationFactor Elevation factor
     * @return 2D coordinates as [x, y]
     */
    private int[] project3DTo2D(double x3d, double y3d, double z3d,
                                int centerX, int centerY,
                                double cosAngle, double sinAngle, double elevationFactor) {
        // Apply isometric projection
        int x2d = centerX + (int) ((x3d * cosAngle - y3d * sinAngle));
        int y2d = centerY + (int) ((x3d * sinAngle + y3d * cosAngle) * elevationFactor - z3d);

        return new int[] {x2d, y2d};
    }

    /**
     * Create a darker shade of a color
     *
     * @param color The base color
     * @param factor The darkening factor (0.0 to 1.0)
     * @return The darkened color
     */
    private Color darken(Color color, double factor) {
        int r = (int) (color.getRed() * factor);
        int g = (int) (color.getGreen() * factor);
        int b = (int) (color.getBlue() * factor);

        return new Color(
                Math.max(0, Math.min(255, r)),
                Math.max(0, Math.min(255, g)),
                Math.max(0, Math.min(255, b))
        );
    }

    /**
     * Handle keyboard shortcuts
     *
     * @param e The key event
     */
    private void handleKeyPress(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_PLUS:
            case KeyEvent.VK_EQUALS:
                // Zoom in
                scale *= 1.1;
                repaint();
                break;
            case KeyEvent.VK_MINUS:
                // Zoom out
                scale /= 1.1;
                repaint();
                break;
            case KeyEvent.VK_LEFT:
                // Rotate view left
                viewAngle = (viewAngle - 5) % 360;
                repaint();
                break;
            case KeyEvent.VK_RIGHT:
                // Rotate view right
                viewAngle = (viewAngle + 5) % 360;
                repaint();
                break;
            case KeyEvent.VK_UP:
                // Increase elevation
                viewElevation = Math.min(viewElevation + 5, 80);
                repaint();
                break;
            case KeyEvent.VK_DOWN:
                // Decrease elevation
                viewElevation = Math.max(viewElevation - 5, 10);
                repaint();
                break;
            case KeyEvent.VK_R:
                // Reset view
                viewAngle = 45;
                viewElevation = 30;
                scale = 40;
                repaint();
                break;
        }
    }

    // Mouse listener methods
    @Override
    public void mouseClicked(MouseEvent e) {
        requestFocusInWindow();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        requestFocusInWindow();
        dragStart = e.getPoint();
        isDragging = true;
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
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
        if (isDragging && dragStart != null) {
            int dx = e.getX() - dragStart.x;

            // Adjust view angle based on horizontal movement
            viewAngle = (viewAngle + dx / 4.0) % 360;

            // Update drag start point
            dragStart = e.getPoint();

            repaint();
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Not used
    }
}