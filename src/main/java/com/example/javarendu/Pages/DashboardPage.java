package com.example.javarendu.Pages;

import com.example.javarendu.App;
import com.example.javarendu.utils.DBconnection;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;

public class DashboardPage {

    private final App app;
    private final Connection connection;

    public DashboardPage(App app, Connection connection) {
        this.app = app;
        this.connection = connection;
    }

    public BorderPane createDashboard() {
        BorderPane dashboardPane = new BorderPane();

        // Create the top navbar
        HBox navbar = new HBox(10);
        navbar.setStyle("-fx-background-color: lightgray; -fx-padding: 10;");
        navbar.setAlignment(Pos.CENTER_LEFT);

        Button dashboardButton = new Button("Dashboard");
        dashboardButton.setOnAction(e -> app.showDashboardPage());

        Button utilisateursButton = new Button("Utilisateurs");
        utilisateursButton.setOnAction(e -> openUtilisateurPage());

        Button sallesButton = new Button("Salles");
        sallesButton.setOnAction(e -> openSallesPage());

        Button evenementsButton = new Button("Evenements");
        evenementsButton.setOnAction(e -> {
            try {
                openEvenementPage();
            } catch (SQLException ex) {
                showAlert("Erreur", "Impossible d'ouvrir la page des événements : " + ex.getMessage());
            }
        });

        Button reservationsButton = new Button("Reservations");
        reservationsButton.setOnAction(e -> openReservationPage());

        Button terrainsButton = new Button("Terrains");
        terrainsButton.setOnAction(e -> openTerrainPage());

        navbar.getChildren().addAll(dashboardButton, utilisateursButton, sallesButton, evenementsButton, reservationsButton, terrainsButton);
        dashboardPane.setTop(navbar);

        // Display user stats and ongoing events (main dashboard content)
        dashboardPane.setCenter(new Button("WELCOME , THIS IS YOUR DASHBOARD !"));

        return dashboardPane;
    }

    private void openUtilisateurPage() {
        UtilisateurPage utilisateurPage = new UtilisateurPage(this);
        BorderPane utilisateurPane = new BorderPane();
        utilisateurPane.setCenter(utilisateurPage.getLayout());
        Stage stage = new Stage();
        stage.setTitle("Utilisateurs");
        stage.setScene(new Scene(utilisateurPane, 800, 600));
        stage.show();
    }

    private void openEvenementPage() throws SQLException {
        EvenementPage evenementPage = new EvenementPage(this);
        BorderPane evenementPane = new BorderPane();
        evenementPane.setCenter(evenementPage.getLayout());
        Stage stage = new Stage();
        stage.setTitle("Evenements");
        stage.setScene(new Scene(evenementPane, 800, 600));
        stage.show();
    }

    private void openReservationPage() {


        // Pass the Connection object to ReservationPage constructor
        ReservationPage reservationPage = new ReservationPage(connection);

        BorderPane reservationPane = new BorderPane();
        reservationPane.setCenter(reservationPage.getLayout());

        Stage stage = new Stage();
        stage.setTitle("Reservations");
        stage.setScene(new Scene(reservationPane, 800, 600));
        stage.show();
    }

    private void openSallesPage() {
        SallePage sallesPage = new SallePage();
        Stage stage = new Stage();
        sallesPage.show(stage);
    }

    private void openTerrainPage() {
        TerrainPage terrainPage = new TerrainPage(connection); // Pass connection to TerrainPage
        Stage stage = new Stage();
        terrainPage.show(stage); // Correctly call the show method
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
