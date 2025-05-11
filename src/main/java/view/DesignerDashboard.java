package org.example.hci.view;

import org.example.hci.controller.DesignController;
import org.example.hci.model.Design;
import org.example.hci.model.FurnitureItem;
import org.example.hci.model.Room;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Main dashboard for furniture designers with enhanced modern UI
 */
public class DesignerDashboard extends JFrame {
    private JPanel mainPanel;
    private JList<Design> savedDesignsList;
    private DefaultListModel<Design> designListModel;
    private DesignController designController;

    // Panels for different sections
    private JPanel toolbarPanel;
    private JPanel workspacePanel;
    private JPanel propertiesPanel;

    // Color scheme
    private final Color PRIMARY_COLOR = new Color(63, 81, 181);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(231, 76, 60);
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245);
    private final Color PANEL_COLOR = new Color(255, 255, 255);
    private final Color TEXT_COLOR = new Color(44, 62, 80);
    private final Color BORDER_COLOR = new Color(189, 195, 199);

    // Fonts
    private final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 14);
    private final Font REGULAR_FONT = new Font("Segoe UI", Font.PLAIN, 12);
    private final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 12);

    public DesignerDashboard() {
        designController = new DesignController();

        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configure the frame
        setTitle("Furniture Design Studio");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Set global UI properties
        configureUIDefaults();

        // Create the main panel with a border layout
        mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Create toolbar at the top
        createToolbar();

        // Create the main split pane
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);
        splitPane.setBackground(BACKGROUND_COLOR);

        // Create the sidebar with saved designs
        createSidebar();

        // Create the workspace
        createWorkspace();

        // Create the properties panel
        createPropertiesPanel();

        // Add sidebar to the left of split pane
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setBackground(PANEL_COLOR);
        leftPanel.setBorder(createPanelBorder("Saved Designs"));

        JScrollPane scrollPane = new JScrollPane(savedDesignsList);
        scrollPane.setBorder(null);
        scrollPane.getViewport().setBackground(PANEL_COLOR);

        leftPanel.add(scrollPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(leftPanel);

        // Add workspace and properties panel to the right of split pane
        JPanel rightPanel = new JPanel(new BorderLayout(10, 0));
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.add(workspacePanel, BorderLayout.CENTER);
        rightPanel.add(propertiesPanel, BorderLayout.EAST);
        splitPane.setRightComponent(rightPanel);

        // Add components to main panel
        mainPanel.add(toolbarPanel, BorderLayout.NORTH);
        mainPanel.add(splitPane, BorderLayout.CENTER);

        // Add status bar
        JPanel statusBar = createStatusBar();
        mainPanel.add(statusBar, BorderLayout.SOUTH);

        // Add main panel to frame
        add(mainPanel);
    }

    private void configureUIDefaults() {
        UIManager.put("Panel.background", PANEL_COLOR);
        UIManager.put("Button.background", PRIMARY_COLOR);
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.font", BUTTON_FONT);
        UIManager.put("ToggleButton.background", SECONDARY_COLOR);
        UIManager.put("ToggleButton.foreground", Color.WHITE);
        UIManager.put("ToggleButton.font", BUTTON_FONT);
        UIManager.put("Label.font", REGULAR_FONT);
        UIManager.put("Label.foreground", TEXT_COLOR);
        UIManager.put("TextField.font", REGULAR_FONT);
        UIManager.put("TextField.background", Color.WHITE);
        UIManager.put("TextField.foreground", TEXT_COLOR);
        UIManager.put("ComboBox.font", REGULAR_FONT);
        UIManager.put("ComboBox.background", Color.WHITE);
        UIManager.put("ComboBox.foreground", TEXT_COLOR);
        UIManager.put("List.font", REGULAR_FONT);
        UIManager.put("List.background", PANEL_COLOR);
        UIManager.put("List.foreground", TEXT_COLOR);
        UIManager.put("TitledBorder.font", HEADER_FONT);
        UIManager.put("TitledBorder.titleColor", PRIMARY_COLOR);
    }

    private Border createPanelBorder(String title) {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createEmptyBorder(2, 2, 2, 2),
                        BorderFactory.createTitledBorder(
                                BorderFactory.createEmptyBorder(5, 5, 5, 5),
                                title,
                                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                                HEADER_FONT,
                                PRIMARY_COLOR
                        )
                )
        );
    }

    private void createToolbar() {
        toolbarPanel = new JPanel();
        toolbarPanel.setLayout(new BoxLayout(toolbarPanel, BoxLayout.X_AXIS));
        toolbarPanel.setBackground(PRIMARY_COLOR);
        toolbarPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create a logo/title panel
        JLabel titleLabel = new JLabel("Furniture Design Studio");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setIcon(createIcon("furniture", 24, 24)); // Replace with actual icon if available

        // Create button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        buttonPanel.setOpaque(false);

        JButton newDesignButton = createToolbarButton("New Design", "new");
        newDesignButton.setPreferredSize(new Dimension(120, 30));
        JButton saveButton = createToolbarButton("Save", "save");
        saveButton.setPreferredSize(new Dimension(120, 30));
        JButton openButton = createToolbarButton("Open", "open");
        openButton.setPreferredSize(new Dimension(120, 30));
        JButton deleteButton = createToolbarButton("Delete", "delete");
        deleteButton.setPreferredSize(new Dimension(120, 30));


        // Add action listeners
        newDesignButton.addActionListener(e -> createNewDesign());
        saveButton.addActionListener(e -> saveCurrentDesign());
        openButton.addActionListener(e -> openSelectedDesign());
        deleteButton.addActionListener(e -> deleteSelectedDesign());

        // Add buttons to the button panel
        buttonPanel.add(newDesignButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(saveButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(openButton);
        buttonPanel.add(Box.createHorizontalStrut(5));
        buttonPanel.add(deleteButton);

        // Add separator
        JSeparator separator = new JSeparator(SwingConstants.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 24));
        separator.setForeground(new Color(255, 255, 255, 100));

        // View toggle buttons
        JToggleButton view2DButton = createToggleButton("2D View", "2d");
        view2DButton.setPreferredSize(new Dimension(120, 30));
        JToggleButton view3DButton = createToggleButton("3D View", "3d");
        view3DButton.setPreferredSize(new Dimension(120, 30));

        ButtonGroup viewGroup = new ButtonGroup();
        viewGroup.add(view2DButton);
        viewGroup.add(view3DButton);

        view2DButton.setSelected(true);

        view2DButton.addActionListener(e -> switchTo2DView());
        view3DButton.addActionListener(e -> switchTo3DView());

        // Add view buttons to button panel
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(separator);
        buttonPanel.add(Box.createHorizontalStrut(10));
        buttonPanel.add(view2DButton);
        buttonPanel.add(view3DButton);

        // Add components to toolbar
        toolbarPanel.add(titleLabel);
        toolbarPanel.add(Box.createHorizontalGlue());
        toolbarPanel.add(buttonPanel);
    }

    private JButton createToolbarButton(String text, String iconName) {
        JButton button = new JButton(text);
        button.setIcon(createIcon(iconName, 16, 16)); // Replace with actual icon if available
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(5, 10, 5, 10));

        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
                button.setContentAreaFilled(true);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setContentAreaFilled(false);
            }
        });

        return button;
    }

    private JToggleButton createToggleButton(String text, String iconName) {
        JToggleButton button = new JToggleButton(text);
        button.setIcon(createIcon(iconName, 16, 16)); // Replace with actual icon if available
        button.setFont(BUTTON_FONT);
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setMargin(new Insets(5, 10, 5, 10));

        // Add hover and selected effects
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setBackground(SECONDARY_COLOR);
                    button.setContentAreaFilled(true);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!button.isSelected()) {
                    button.setContentAreaFilled(false);
                }
            }
        });

        return button;
    }

    private ImageIcon createIcon(String name, int width, int height) {
        // Placeholder for actual icon creation
        // In a real application, you would load actual icons here
        return new ImageIcon();
    }

    private void createSidebar() {
        designListModel = new DefaultListModel<>();
        savedDesignsList = new JList<>(designListModel);
        savedDesignsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        savedDesignsList.setCellRenderer(new DesignListCellRenderer());
        savedDesignsList.setFixedCellHeight(60);
        savedDesignsList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Add hover effect
        savedDesignsList.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int index = savedDesignsList.locationToIndex(e.getPoint());
                if (index >= 0) {
                    savedDesignsList.setSelectedIndex(index);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                savedDesignsList.clearSelection();
            }
        });

        // Load saved designs
        List<Design> savedDesigns = designController.getSavedDesigns();
        for (Design design : savedDesigns) {
            designListModel.addElement(design);
        }
    }

    private void createWorkspace() {
        workspacePanel = new JPanel(new BorderLayout());
        workspacePanel.setBackground(PANEL_COLOR);
        workspacePanel.setBorder(createPanelBorder("Design Workspace"));

        // Initially show an empty 2D canvas
        DesignCanvas2D designCanvas = new DesignCanvas2D();
        designCanvas.setBackground(Color.WHITE);
        workspacePanel.add(designCanvas, BorderLayout.CENTER);
    }

    private void createPropertiesPanel() {
        propertiesPanel = new JPanel();
        propertiesPanel.setPreferredSize(new Dimension(270, getHeight()));
        propertiesPanel.setBackground(PANEL_COLOR);
        propertiesPanel.setBorder(createPanelBorder("Properties"));

        // Create a form layout for properties
        propertiesPanel.setLayout(new BoxLayout(propertiesPanel, BoxLayout.Y_AXIS));

        // Room properties section
        JPanel roomPanel = createPropertiesSection("Room Settings");

        JPanel roomGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        roomGrid.setOpaque(false);

        roomGrid.add(createLabel("Width (m):"));
        JTextField widthField = createTextField("5.0");
        roomGrid.add(widthField);

        roomGrid.add(createLabel("Length (m):"));
        JTextField lengthField = createTextField("6.0");
        roomGrid.add(lengthField);

        roomGrid.add(createLabel("Height (m):"));
        JTextField heightField = createTextField("2.5");
        roomGrid.add(heightField);

        roomGrid.add(createLabel("Wall Color:"));
        JButton wallColorButton = createColorButton("Select");
        wallColorButton.addActionListener(e -> selectColor("Wall Color"));
        roomGrid.add(wallColorButton);

        roomGrid.add(createLabel("Floor Color:"));
        JButton floorColorButton = createColorButton("Select");
        floorColorButton.addActionListener(e -> selectColor("Floor Color"));
        roomGrid.add(floorColorButton);

        roomPanel.add(roomGrid);

        // Furniture properties section
        JPanel furniturePanel = createPropertiesSection("Furniture Settings");

        JPanel furnitureGrid = new JPanel(new GridLayout(0, 2, 10, 10));
        furnitureGrid.setOpaque(false);

        String[] furnitureTypes = {"Chair", "Table", "Sofa", "Bed", "Cabinet"};
        JComboBox<String> furnitureComboBox = new JComboBox<>(furnitureTypes);
        furnitureComboBox.setBackground(Color.WHITE);

        furnitureGrid.add(createLabel("Type:"));
        furnitureGrid.add(furnitureComboBox);

        furnitureGrid.add(createLabel("Color:"));
        JButton furnitureColorButton = createColorButton("Select");
        furnitureColorButton.addActionListener(e -> selectColor("Furniture Color"));
        furnitureGrid.add(furnitureColorButton);

        furniturePanel.add(furnitureGrid);

        JButton addFurnitureButton = new JButton("Add Furniture");
        addFurnitureButton.setBackground(PRIMARY_COLOR);
        addFurnitureButton.setForeground(Color.WHITE);
        addFurnitureButton.setFont(BUTTON_FONT);
        addFurnitureButton.setFocusPainted(false);
        addFurnitureButton.setBorderPainted(false);
        addFurnitureButton.setMargin(new Insets(10, 20, 10, 20));
        addFurnitureButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        addFurnitureButton.addActionListener(e -> addFurniture((String) furnitureComboBox.getSelectedItem()));

        // Add all panels to properties panel
        propertiesPanel.add(roomPanel);
        propertiesPanel.add(Box.createRigidArea(new Dimension(0, 15)));
        propertiesPanel.add(furniturePanel);
        propertiesPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.add(Box.createHorizontalGlue());
        buttonPanel.add(addFurnitureButton);
        buttonPanel.add(Box.createHorizontalGlue());

        propertiesPanel.add(buttonPanel);
        propertiesPanel.add(Box.createVerticalGlue());
    }

    private JPanel createPropertiesSection(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(PANEL_COLOR);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                BorderFactory.createTitledBorder(
                        BorderFactory.createEmptyBorder(5, 5, 10, 5),
                        title,
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        HEADER_FONT,
                        PRIMARY_COLOR
                )
        ));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(REGULAR_FONT);
        label.setForeground(TEXT_COLOR);
        return label;
    }

    private JTextField createTextField(String text) {
        JTextField textField = new JTextField(text);
        textField.setFont(REGULAR_FONT);
        textField.setForeground(TEXT_COLOR);
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return textField;
    }

    private JButton createColorButton(String text) {
        JButton button = new JButton(text);
        button.setFont(REGULAR_FONT);
        button.setForeground(TEXT_COLOR);
        button.setBackground(Color.WHITE);
        button.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        button.setFocusPainted(false);
        return button;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBackground(new Color(236, 240, 241));
        statusBar.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_COLOR));
        statusBar.setPreferredSize(new Dimension(getWidth(), 25));

        JLabel statusLabel = new JLabel(" Ready");
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        statusLabel.setForeground(new Color(127, 140, 141));

        statusBar.add(statusLabel, BorderLayout.WEST);

        return statusBar;
    }

    // Action methods
    private void createNewDesign() {
        // Show dialog to create new room
        JTextField nameField = createTextField("");
        JTextField widthField = createTextField("5.0");
        JTextField lengthField = createTextField("6.0");
        JTextField heightField = createTextField("2.5");

        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(createLabel("Design Name:"));
        panel.add(nameField);
        panel.add(createLabel("Room Width (m):"));
        panel.add(widthField);
        panel.add(createLabel("Room Length (m):"));
        panel.add(lengthField);
        panel.add(createLabel("Room Height (m):"));
        panel.add(heightField);

        JOptionPane pane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION);

        JDialog dialog = pane.createDialog(this, "Create New Design");
        dialog.setVisible(true);

        Integer result = (Integer) pane.getValue();
        if (result != null && result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                double width = Double.parseDouble(widthField.getText());
                double length = Double.parseDouble(lengthField.getText());
                double height = Double.parseDouble(heightField.getText());

                // Create new room and design
                Room room = new Room(width, length, height);
                Design design = new Design(name, room);

                // Create a new canvas with the room
                designController.setCurrentDesign(design);
                refreshWorkspace();

            } catch (NumberFormatException ex) {
                showErrorMessage("Please enter valid numbers for dimensions", "Invalid Input");
            }
        }
    }

    private void saveCurrentDesign() {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            designController.saveDesign(currentDesign);

            // Update list if needed
            if (!designListModel.contains(currentDesign)) {
                designListModel.addElement(currentDesign);
            }

            showInfoMessage("Design saved successfully", "Save Complete");
        } else {
            showErrorMessage("No design to save", "Save Error");
        }
    }

    private void openSelectedDesign() {
        Design selectedDesign = savedDesignsList.getSelectedValue();
        if (selectedDesign != null) {
            designController.setCurrentDesign(selectedDesign);
            refreshWorkspace();
        }
    }

    private void deleteSelectedDesign() {
        Design selectedDesign = savedDesignsList.getSelectedValue();
        if (selectedDesign != null) {
            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to delete this design?",
                    "Confirm Delete",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                designController.deleteDesign(selectedDesign);
                designListModel.removeElement(selectedDesign);
            }
        }
    }

    private void switchTo2DView() {
        workspacePanel.removeAll();

        // Create a 2D canvas with current design
        DesignCanvas2D canvas = new DesignCanvas2D();
        canvas.setDesign(designController.getCurrentDesign());
        canvas.setBackground(Color.WHITE);

        workspacePanel.add(canvas, BorderLayout.CENTER);
        workspacePanel.revalidate();
        workspacePanel.repaint();
    }

    private void switchTo3DView() {
        workspacePanel.removeAll();

        // Create a 3D canvas with current design
        DesignCanvas3D canvas = new DesignCanvas3D();
        canvas.setDesign(designController.getCurrentDesign());
        canvas.setBackground(Color.WHITE);

        workspacePanel.add(canvas, BorderLayout.CENTER);
        workspacePanel.revalidate();
        workspacePanel.repaint();
    }

    private void refreshWorkspace() {
        // Refresh the current view (2D or 3D)
        // Determine which view is active and refresh it
        for (Component component : workspacePanel.getComponents()) {
            if (component instanceof DesignCanvas2D) {
                ((DesignCanvas2D) component).setDesign(designController.getCurrentDesign());
                component.repaint();
            } else if (component instanceof DesignCanvas3D) {
                ((DesignCanvas3D) component).setDesign(designController.getCurrentDesign());
                component.repaint();
            }
        }
    }

    private void selectColor(String colorType) {
        Color initialColor = Color.WHITE;
        Color selectedColor = JColorChooser.showDialog(
                this, "Select " + colorType, initialColor);

        if (selectedColor != null) {
            // Update the appropriate color in the design
            Design currentDesign = designController.getCurrentDesign();
            if (currentDesign != null) {
                if (colorType.equals("Wall Color")) {
                    currentDesign.getRoom().setWallColor(selectedColor);
                } else if (colorType.equals("Floor Color")) {
                    currentDesign.getRoom().setFloorColor(selectedColor);
                } else if (colorType.equals("Furniture Color")) {
                    // This would be applied to newly added furniture
                    designController.setCurrentFurnitureColor(selectedColor);
                }
                refreshWorkspace();
            }
        }
    }

    private void addFurniture(String furnitureType) {
        Design currentDesign = designController.getCurrentDesign();
        if (currentDesign != null) {
            FurnitureItem.FurnitureType type = FurnitureItem.FurnitureType.valueOf(furnitureType.toUpperCase());

            // Find the active canvas and set the pending furniture
            for (Component component : workspacePanel.getComponents()) {
                if (component instanceof DesignCanvas2D) {
                    ((DesignCanvas2D) component).setPendingFurniture(type);
                    break;
                }
            }

            showInfoMessage("Added " + furnitureType + " to design. Click in the workspace to place it.", "Furniture Added");
        } else {
            showWarningMessage("Please create or open a design first", "No Active Design");
        }
    }

    private void showInfoMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWarningMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.WARNING_MESSAGE);
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(
                this,
                message,
                title,
                JOptionPane.ERROR_MESSAGE);
    }

    // Custom list cell renderer for designs with enhanced visual style
    private class DesignListCellRenderer extends DefaultListCellRenderer {
        @Override
        public Component getListCellRendererComponent(
                JList<?> list, Object value, int index,
                boolean isSelected, boolean cellHasFocus) {

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 0, BORDER_COLOR),
                    BorderFactory.createEmptyBorder(10, 10, 10, 10)
            ));

            if (isSelected) {
                panel.setBackground(new Color(236, 240, 241));
                panel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createMatteBorder(0, 3, 0, 0, PRIMARY_COLOR),
                        BorderFactory.createEmptyBorder(10, 7, 10, 10)
                ));
            } else {
                panel.setBackground(PANEL_COLOR);
            }

            if (value instanceof Design) {
                Design design = (Design) value;

                // Create a thumbnail icon (placeholder)
                JPanel thumbnailPanel = new JPanel();
                thumbnailPanel.setPreferredSize(new Dimension(40, 40));
                thumbnailPanel.setBackground(design.getRoom().getWallColor());
                thumbnailPanel.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));

                // Create name label
                JLabel nameLabel = new JLabel(design.getName());
                nameLabel.setFont(HEADER_FONT);
                nameLabel.setForeground(TEXT_COLOR);

                // Create details label
                Room room = design.getRoom();
                JLabel detailsLabel = new JLabel(String.format("%.1fm × %.1fm × %.1fm",
                        room.getWidth(), room.getLength(), room.getHeight()));
                detailsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
                detailsLabel.setForeground(new Color(127, 140, 141));

                // Add components to panel
                JPanel textPanel = new JPanel(new GridLayout(2, 1));
                textPanel.setOpaque(false);
                textPanel.add(nameLabel);
                textPanel.add(detailsLabel);

                panel.add(thumbnailPanel, BorderLayout.WEST);
                panel.add(textPanel, BorderLayout.CENTER);
            }

            return panel;
        }
    }
}