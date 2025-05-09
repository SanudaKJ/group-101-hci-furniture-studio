package org.example.hci;


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

    }
}