package com.example.javarendu;

import com.example.javarendu.Pages.DashboardPage;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class App extends Application {

    private Stage primaryStage;
    private Connection connection;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Establish database connection
        try {
            connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/RenduDB", "postgres", "anass123");

            // Initialize the DashboardPage
            DashboardPage dashboardPage = new DashboardPage(this, connection);

            // Set the Scene
            BorderPane root = new BorderPane();
            root.setCenter(dashboardPage.createDashboard());
            Scene scene = new Scene(root, 800, 600);
            primaryStage.setTitle("Dashboard");
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (SQLException ex) {
            System.err.println("Failed to connect to the database: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    // Method to go back to the dashboard
    public void showDashboardPage() {
        DashboardPage dashboardPage = new DashboardPage(this, connection);
        BorderPane root = new BorderPane();
        root.setCenter(dashboardPage.createDashboard());
        primaryStage.setScene(new Scene(root, 800, 600));
    }

    @Override
    public void stop() {
        // Close the database connection when the application stops
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                System.err.println("Failed to close the database connection: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
