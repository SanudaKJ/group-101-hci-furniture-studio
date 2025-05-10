package org.example.hci.view;

import org.example.hci.auth.MongoDBAuthService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Enhanced login screen for designer authentication with MongoDB integration
 */
public class LoginView extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton signUpButton;
    private JLabel statusLabel;
    private JCheckBox rememberMeCheckbox;

    private MongoDBAuthService authService;

    // Colors
    private final Color PRIMARY_COLOR = new Color(63, 81, 181); // Indigo
    private final Color ACCENT_COLOR = new Color(255, 152, 0);  // Orange
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private final Color ERROR_COLOR = new Color(244, 67, 54); // Red

    public LoginView() {
        try {
            // Initialize the MongoDB authentication service
            authService = new MongoDBAuthService();

            // Configure the frame
            setTitle("Furniture Design Studio - Login");
            setSize(900, 600);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            setUndecorated(true); // Remove default window decorations
            setShape(new RoundRectangle2D.Double(0, 0, 900, 600, 15, 15)); // Rounded corners

            // Create main panel with a custom background
            JPanel mainPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();

                    // Enable antialiasing
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint background gradient
                    GradientPaint gradient = new GradientPaint(
                            0, 0, BACKGROUND_COLOR,
                            getWidth(), getHeight(), new Color(235, 235, 235)
                    );
                    g2d.setPaint(gradient);
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    g2d.dispose();
                }
            };
            mainPanel.setLayout(new BorderLayout());

            // Create a split panel: left for image, right for login form
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(450);
            splitPane.setEnabled(false); // Disable dragging
            splitPane.setDividerSize(0); // Hide divider

            // Left panel with illustration
            JPanel leftPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2d = (Graphics2D) g.create();

                    // Enable antialiasing
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Paint background
                    g2d.setColor(PRIMARY_COLOR);
                    g2d.fillRect(0, 0, getWidth(), getHeight());

                    // Draw some furniture-like shapes
                    g2d.setColor(new Color(255, 255, 255, 100));

                    // Draw a sofa
                    g2d.fillRoundRect(100, 200, 250, 100, 20, 20);
                    g2d.fillRoundRect(100, 170, 40, 30, 10, 10);
                    g2d.fillRoundRect(310, 170, 40, 30, 10, 10);

                    // Draw a table
                    g2d.fillRect(150, 350, 150, 10);
                    g2d.fillRect(160, 360, 10, 50);
                    g2d.fillRect(280, 360, 10, 50);

                    // Draw a lamp
                    g2d.fillRect(350, 350, 5, 70);
                    g2d.fillOval(330, 320, 45, 30);

                    // Company name
                    g2d.setFont(new Font("Segoe UI", Font.BOLD, 30));
                    g2d.setColor(Color.WHITE);
                    g2d.drawString("FURNITURE DESIGN STUDIO", 20, 100);

                    // Tagline
                    g2d.setFont(new Font("Segoe UI", Font.ITALIC, 16));
                    g2d.drawString("Transform your space with creative designs", 50, 130);

                    g2d.dispose();
                }
            };

            // Right panel for login form
            JPanel rightPanel = new JPanel();
            rightPanel.setLayout(new BorderLayout());
            rightPanel.setBackground(BACKGROUND_COLOR);

            // Add a close button
            JPanel topBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            topBar.setOpaque(false);
            JLabel closeLabel = new JLabel("×");
            closeLabel.setFont(new Font("Arial", Font.BOLD, 24));
            closeLabel.setForeground(TEXT_COLOR);
            closeLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            closeLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    System.exit(0);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    closeLabel.setForeground(ERROR_COLOR);
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    closeLabel.setForeground(TEXT_COLOR);
                }
            });
            topBar.add(closeLabel);
            rightPanel.add(topBar, BorderLayout.NORTH);

            // Login form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setOpaque(false);
            formPanel.setBorder(new EmptyBorder(20, 40, 40, 40));

            // Login header
            JLabel headerLabel = new JLabel("Designer Login");
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            headerLabel.setForeground(PRIMARY_COLOR);
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Welcome message
            JLabel welcomeLabel = new JLabel("Welcome back! Please enter your credentials.");
            welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            welcomeLabel.setForeground(TEXT_COLOR);
            welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Username field with custom styling
            JPanel usernamePanel = new JPanel();
            usernamePanel.setLayout(new BorderLayout());
            usernamePanel.setOpaque(false);
            usernamePanel.setMaximumSize(new Dimension(300, 80));
            usernamePanel.setBorder(new EmptyBorder(20, 0, 0, 0));

            JLabel usernameLabel = new JLabel("Username");
            usernameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            usernameLabel.setForeground(TEXT_COLOR);

            usernameField = new JTextField(20) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    super.paintComponent(g);
                }
            };
            usernameField.setOpaque(false);
            usernameField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            usernamePanel.add(usernameLabel, BorderLayout.NORTH);
            usernamePanel.add(usernameField, BorderLayout.CENTER);

            // Password field with custom styling
            JPanel passwordPanel = new JPanel();
            passwordPanel.setLayout(new BorderLayout());
            passwordPanel.setOpaque(false);
            passwordPanel.setMaximumSize(new Dimension(300, 80));
            passwordPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

            JLabel passwordLabel = new JLabel("Password");
            passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            passwordLabel.setForeground(TEXT_COLOR);

            passwordField = new JPasswordField(20) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                    super.paintComponent(g);
                }
            };
            passwordField.setOpaque(false);
            passwordField.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

            passwordPanel.add(passwordLabel, BorderLayout.NORTH);
            passwordPanel.add(passwordField, BorderLayout.CENTER);

            // Remember me checkbox
            JPanel rememberPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 5));
            rememberPanel.setOpaque(false);
            rememberPanel.setMaximumSize(new Dimension(300, 40));

            rememberMeCheckbox = new JCheckBox("Remember me");
            rememberMeCheckbox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            rememberMeCheckbox.setForeground(TEXT_COLOR);
            rememberMeCheckbox.setOpaque(false);
            rememberMeCheckbox.setFocusPainted(false);

            rememberPanel.add(rememberMeCheckbox);

            // Login button with custom styling
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.setMaximumSize(new Dimension(300, 100));
            buttonPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

            loginButton = new JButton("LOGIN") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    if (getModel().isPressed()) {
                        g2.setColor(PRIMARY_COLOR.darker());
                    } else if (getModel().isRollover()) {
                        g2.setColor(PRIMARY_COLOR.brighter());
                    } else {
                        g2.setColor(PRIMARY_COLOR);
                    }

                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                    super.paintComponent(g);
                }
            };
            loginButton.setPreferredSize(new Dimension(200, 45));
            loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
            loginButton.setForeground(Color.WHITE);
            loginButton.setBorderPainted(false);
            loginButton.setFocusPainted(false);
            loginButton.setContentAreaFilled(false);
            loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            loginButton.addActionListener(this);

            buttonPanel.add(loginButton);

            // Sign Up Option
            JPanel signUpPanel = new JPanel();
            signUpPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            signUpPanel.setOpaque(false);
            signUpPanel.setMaximumSize(new Dimension(300, 40));

            signUpButton = new JButton("New designer? Sign up");
            signUpButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            signUpButton.setForeground(PRIMARY_COLOR);
            signUpButton.setBorderPainted(false);
            signUpButton.setContentAreaFilled(false);
            signUpButton.setFocusPainted(false);
            signUpButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            signUpButton.addActionListener(this);

            signUpPanel.add(signUpButton);

            // Status label for error messages
            statusLabel = new JLabel("");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statusLabel.setForeground(ERROR_COLOR);
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add components to form panel
            formPanel.add(Box.createVerticalGlue());
            formPanel.add(headerLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(welcomeLabel);
            formPanel.add(usernamePanel);
            formPanel.add(passwordPanel);
            formPanel.add(rememberPanel);
            formPanel.add(buttonPanel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(statusLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(signUpPanel);
            formPanel.add(Box.createVerticalGlue());

            // Add form panel to right panel
            rightPanel.add(formPanel, BorderLayout.CENTER);

            // Add panels to split pane
            splitPane.setLeftComponent(leftPanel);
            splitPane.setRightComponent(rightPanel);

            // Add split pane to main panel
            mainPanel.add(splitPane, BorderLayout.CENTER);

            // Make the frame draggable from anywhere
            MouseAdapter frameMouseAdapter = new MouseAdapter() {
                private Point dragStart;

                @Override
                public void mousePressed(MouseEvent e) {
                    dragStart = e.getPoint();
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null) {
                        Point dragEnd = e.getPoint();
                        int dx = dragEnd.x - dragStart.x;
                        int dy = dragEnd.y - dragStart.y;

                        Point windowLocation = getLocation();
                        setLocation(windowLocation.x + dx, windowLocation.y + dy);
                    }
                }
            };

            addMouseListener(frameMouseAdapter);
            addMouseMotionListener(frameMouseAdapter);

            // Add main panel to frame
            add(mainPanel);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error initializing the application. Please make sure MongoDB is running.\n\nError: " + e.getMessage(),
                    "Database Connection Error",
                    JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // Validate inputs
            if (username.isEmpty() || password.isEmpty()) {
                statusLabel.setText("Please enter both username and password");
                return;
            }

            loginButton.setEnabled(false);
            loginButton.setText("LOGGING IN...");

            // Simulate a slight delay to add a more realistic feel
            Timer timer = new Timer(800, event -> {
                try {
                    if (authService.authenticate(username, password)) {
                        // Update last login time
                        authService.updateLastLogin(username);

                        // Open the main application window
                        openMainApplication();
                    } else {
                        statusLabel.setText("Invalid username or password");
                        loginButton.setEnabled(true);
                        loginButton.setText("LOGIN");
                    }
                } catch (Exception ex) {
                    statusLabel.setText("Login error: " + ex.getMessage());
                    loginButton.setEnabled(true);
                    loginButton.setText("LOGIN");
                    ex.printStackTrace();
                }
            });
            timer.setRepeats(false);
            timer.start();
        } else if (e.getSource() == signUpButton) {
            // Open the sign up screen
            openSignUpScreen();
        }
    }

    private void openMainApplication() {
        // Close login window
        setVisible(false);
        dispose();

        // Open main application window
        SwingUtilities.invokeLater(() -> {
            DesignerDashboard dashboard = new DesignerDashboard();
            dashboard.setVisible(true);
        });
    }

    private void openSignUpScreen() {
        // Close login window
        setVisible(false);
        dispose();

        // Open sign up window
        SwingUtilities.invokeLater(() -> {
            SignUpView signUpView = new SignUpView();
            signUpView.setVisible(true);
        });
    }

    // For testing - remove in production
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            try {
                LoginView loginView = new LoginView();
                loginView.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null,
                        "Error starting application: " + e.getMessage(),
                        "Application Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}