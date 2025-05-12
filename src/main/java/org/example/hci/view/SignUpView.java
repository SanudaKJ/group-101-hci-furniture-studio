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
 * Sign up screen for new designer registration with MongoDB integration
 */
public class SignUpView extends JFrame implements ActionListener {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JTextField emailField;
    private JTextField fullNameField;
    private JButton signUpButton;
    private JButton backToLoginButton;
    private JLabel statusLabel;

    private MongoDBAuthService authService;

    // Colors - matching LoginView
    private final Color PRIMARY_COLOR = new Color(63, 81, 181); // Indigo
    private final Color ACCENT_COLOR = new Color(255, 152, 0);  // Orange
    private final Color BACKGROUND_COLOR = new Color(245, 245, 245); // Light gray
    private final Color TEXT_COLOR = new Color(33, 33, 33); // Dark gray
    private final Color ERROR_COLOR = new Color(244, 67, 54); // Red
    private final Color SUCCESS_COLOR = new Color(76, 175, 80); // Green

    public SignUpView() {
        try {
            // Initialize MongoDB authentication service
            authService = new MongoDBAuthService();

            // Configure the frame
            setTitle("Furniture Design Studio - Sign Up");
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

            // Create a split panel: left for image, right for signup form
            JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
            splitPane.setDividerLocation(450);
            splitPane.setEnabled(false); // Disable dragging
            splitPane.setDividerSize(0); // Hide divider

            // Left panel with illustration - Similar to LoginView
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

                    // Draw some furniture-like shapes - slightly different from login view
                    g2d.setColor(new Color(255, 255, 255, 100));

                    // Draw a chair
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
                    g2d.drawString("Join our creative designer community", 50, 130);

                    g2d.dispose();
                }
            };

            // Right panel for signup form
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

            // Signup form panel
            JPanel formPanel = new JPanel();
            formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
            formPanel.setOpaque(false);
            formPanel.setBorder(new EmptyBorder(0, 40, 20, 40));

            // Signup header
            JLabel headerLabel = new JLabel("Create Account");
            headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
            headerLabel.setForeground(PRIMARY_COLOR);
            headerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Welcome message
            JLabel welcomeLabel = new JLabel("Join our designer community and start creating!");
            welcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            welcomeLabel.setForeground(TEXT_COLOR);
            welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Full Name field
            JPanel fullNamePanel = createFormFieldPanel("Full Name");
            fullNameField = createStyledTextField();
            fullNamePanel.add(fullNameField, BorderLayout.CENTER);

            // Email field
            JPanel emailPanel = createFormFieldPanel("Email Address");
            emailField = createStyledTextField();
            emailPanel.add(emailField, BorderLayout.CENTER);

            // Username field
            JPanel usernamePanel = createFormFieldPanel("Username");
            usernameField = createStyledTextField();
            usernamePanel.add(usernameField, BorderLayout.CENTER);

            // Password field
            JPanel passwordPanel = createFormFieldPanel("Password");
            passwordField = createStyledPasswordField();
            passwordPanel.add(passwordField, BorderLayout.CENTER);

            // Confirm Password field
            JPanel confirmPasswordPanel = createFormFieldPanel("Confirm Password");
            confirmPasswordField = createStyledPasswordField();
            confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);

            // Sign Up button with custom styling
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            buttonPanel.setOpaque(false);
            buttonPanel.setMaximumSize(new Dimension(300, 100));
            buttonPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

            signUpButton = createStyledButton("SIGN UP", PRIMARY_COLOR);
            signUpButton.addActionListener(this);
            buttonPanel.add(signUpButton);

            // Back to Login button
            JPanel backButtonPanel = new JPanel();
            backButtonPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            backButtonPanel.setOpaque(false);
            backButtonPanel.setMaximumSize(new Dimension(300, 40));

            backToLoginButton = new JButton("Already have an account? Login");
            backToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            backToLoginButton.setForeground(PRIMARY_COLOR);
            backToLoginButton.setBorderPainted(false);
            backToLoginButton.setContentAreaFilled(false);
            backToLoginButton.setFocusPainted(false);
            backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            backToLoginButton.addActionListener(this);
            backButtonPanel.add(backToLoginButton);

            // Status label for error/success messages
            statusLabel = new JLabel("");
            statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            statusLabel.setForeground(ERROR_COLOR);
            statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            // Add components to form panel
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(headerLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            formPanel.add(welcomeLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 10)));
            formPanel.add(fullNamePanel);
            formPanel.add(emailPanel);
            formPanel.add(usernamePanel);
            formPanel.add(passwordPanel);
            formPanel.add(confirmPasswordPanel);
            formPanel.add(buttonPanel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            formPanel.add(statusLabel);
            formPanel.add(Box.createRigidArea(new Dimension(0, 5)));
            formPanel.add(backButtonPanel);

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

    private JPanel createFormFieldPanel(String labelText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(300, 65));
        panel.setBorder(new EmptyBorder(5, 0, 0, 0));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_COLOR);

        panel.add(label, BorderLayout.NORTH);
        return panel;
    }

    private JTextField createStyledTextField() {
        JTextField textField = new JTextField(20) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 10, 10);
                super.paintComponent(g);
            }
        };
        textField.setOpaque(false);
        textField.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return textField;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField passwordField = new JPasswordField(20) {
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
        passwordField.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return passwordField;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                super.paintComponent(g);
            }
        };
        button.setPreferredSize(new Dimension(200, 45));
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == signUpButton) {
            // Validate inputs
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // Perform validation
            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() ||
                    password.isEmpty() || confirmPassword.isEmpty()) {
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Please fill in all fields");
                return;
            }

            if (!isValidEmail(email)) {
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Please enter a valid email address");
                return;
            }

            if (password.length() < 6) {
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Password must be at least 6 characters long");
                return;
            }

            if (!password.equals(confirmPassword)) {
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Passwords do not match");
                return;
            }

            // Disable button and show "processing"
            signUpButton.setEnabled(false);
            signUpButton.setText("CREATING ACCOUNT...");

            try {
                // Attempt to register
                System.out.println("Attempting to register user: " + username);
                boolean success = authService.registerUser(username, password, email, fullName);
                System.out.println("Registration result: " + (success ? "Success" : "Failed"));

                if (success) {
                    // Show success message and redirect to login after delay
                    statusLabel.setForeground(SUCCESS_COLOR);
                    statusLabel.setText("Account created successfully!");

                    Timer redirectTimer = new Timer(1500, redirectEvent -> {
                        // Open login screen
                        openLoginScreen();
                    });
                    redirectTimer.setRepeats(false);
                    redirectTimer.start();
                } else {
                    // Show error message
                    statusLabel.setForeground(ERROR_COLOR);
                    statusLabel.setText("Username already exists");
                    signUpButton.setEnabled(true);
                    signUpButton.setText("SIGN UP");
                }
            } catch (Exception ex) {
                // Show error message
                statusLabel.setForeground(ERROR_COLOR);
                statusLabel.setText("Error: " + ex.getMessage());
                signUpButton.setEnabled(true);
                signUpButton.setText("SIGN UP");
                ex.printStackTrace();
            }
        } else if (e.getSource() == backToLoginButton) {
            openLoginScreen();
        }
    }

    private boolean isValidEmail(String email) {
        // Basic email validation regex
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    private void openLoginScreen() {
        // Close sign up window
        setVisible(false);
        dispose();

        // Open login window
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
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
                SignUpView signUpView = new SignUpView();
                signUpView.setVisible(true);
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