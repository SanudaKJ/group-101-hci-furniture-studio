# Furniture Design Studio

A Java Swing application for designing and visualizing furniture layouts in rooms with 2D and 3D capabilities.

## Overview

Furniture Design Studio is an interactive desktop application that enables designers to create, edit, save, and visualize furniture layouts within room spaces. The application features both 2D and 3D visualization capabilities, allowing designers to create professional interior designs with ease.

## Features

- **User Authentication**: Secure login system with MongoDB backend storage
- **2D Design Canvas**: Interactive canvas for placing and arranging furniture items
- **3D Visualization**: View designs in 3D with realistic furniture rendering
- **Room Customization**: Adjust room dimensions and appearance
- **Furniture Library**: Selection of common furniture types (chairs, tables, sofas, beds, cabinets)
- **Design Management**: Save, load, and modify design projects
- **Intuitive UI**: Modern and user-friendly interface with drag-and-drop functionality

## Technical Stack

- **Language**: Java 21
- **UI Framework**: Java Swing
- **Build Tool**: Gradle
- **Database**: MongoDB Atlas (cloud-hosted)
- **Dependencies**:
  - Spring Boot 3.2.0 (core functionality)
  - MongoDB Java Driver 4.10.2
  - JSON library 20231013

## Installation Requirements

- Java Development Kit (JDK) 21 or higher
- Internet connection for MongoDB authentication
- Gradle 8.0+ (included as wrapper)

## MongoDB Configuration

The application uses MongoDB Atlas for data storage. The connection string and database name are configured in `src/main/java/org/example/group-101-hci-furniture-studio/auth/MongoDBConnector.java`.

You can replace the connection string with your own MongoDB instance by modifying:

```java
private static final String CONNECTION_STRING = "connection_string_from_report";
private static final String DATABASE_NAME = "database_name";
```

For security purposes, in a production environment, it's recommended to store the connection string in environment variables or a configuration file that is not committed to version control.

## Getting Started

1. Clone the repository
2. Navigate to the project root directory
3. Build the application:
   ```
   ./gradlew build
   ```
4. Run the application:
   ```
   ./gradlew bootRun
   ```
   
   Alternatively, you can run the generated jar:
   ```
   java -jar build/libs/HCI-0.0.1-SNAPSHOT.jar
   ```

## Test User Credentials

For testing purposes, you can use the following pre-configured user accounts:

| Username  | Password  | Email                 | Role             |
|-----------|-----------|----------------------|------------------|
| designer1 | password1 | designer1@example.com | Default Designer |
| designer2 | password2 | designer2@example.com | Default Designer |
| admin     | admin123  | admin@example.com     | Administrator    |

## Usage Guide

### Login/Registration
1. Launch the application to see the login screen
2. Enter your username and password or click "Sign Up" to create a new account
3. Use the "Remember Me" option to save your login details

### Creating a New Design
1. From the dashboard, click "New Design"
2. Enter room dimensions and select floor/wall colors
3. Use the toolbar to add furniture items to the room
4. Click on the canvas to place the selected furniture item

### Editing a Design
1. Select a furniture item to move, rotate, or delete it
2. Use the properties panel to adjust furniture attributes
3. Use keyboard shortcuts for quick actions:
   - Delete: Remove selected item
   - R: Rotate selected item by 45 degrees
   - Arrow keys: Move selected item
   - Shift + Arrow keys: Pan the view

### Saving and Loading Designs
1. Click "Save Design" to store your current work
2. Previously saved designs appear in the sidebar
3. Click on a saved design to load and continue editing

### 3D Visualization
1. Click the "3D View" tab to switch to 3D visualization
2. Use mouse controls to adjust the camera angle and zoom
3. Toggle between different rendering modes for various visualization options

## Project Structure

- `src/main/java/org/example/hci/`
  - `Main.java`: Application entry point
  - `auth/`: Authentication services and MongoDB connection
  - `controller/`: Application logic controllers
  - `model/`: Data models for designs, rooms and furniture
  - `view/`: UI components and visualizations

## License

This project is proprietary software.

## Acknowledgements

- MongoDB Atlas for database services
- Spring Boot framework
- Java Swing library

---

*© 2025 Furniture Design Studio*
