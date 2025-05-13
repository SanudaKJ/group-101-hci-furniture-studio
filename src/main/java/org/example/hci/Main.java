package org.example.hci;


import org.example.hci.view.LoginView;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Main entry point for the Furniture Design Application
 */
public class Main {
    public static void main(String[] args) {
        try {
            // Set system look and feel
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch application on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
        });
    }
}