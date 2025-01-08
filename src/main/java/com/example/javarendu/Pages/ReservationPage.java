package com.example.javarendu.Pages;

import com.example.javarendu.dao.ReservationDAO;
import com.example.javarendu.model.Reservation;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.StringConverter;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReservationPage {
    private final ReservationDAO reservationDAO;
    private final TableView<Reservation> reservationTable;
    private final ObservableList<Reservation> reservationList;

    private final ComboBox<Integer> userIdComboBox;
    private final ComboBox<Integer> salleIdComboBox;
    private final ComboBox<Integer> terrainIdComboBox;
    private final DatePicker datePicker;
    private final TextField startTimeField;
    private final TextField endTimeField;

    public ReservationPage(Connection connection) {
        this.reservationDAO = new ReservationDAO(connection);
        this.reservationTable = new TableView<>();
        this.reservationList = FXCollections.observableArrayList();

        userIdComboBox = new ComboBox<>();
        salleIdComboBox = new ComboBox<>();
        terrainIdComboBox = new ComboBox<>();
        datePicker = new DatePicker();
        startTimeField = new TextField();
        endTimeField = new TextField();

        loadReservationData();
    }

    public BorderPane getLayout() {
        BorderPane layout = new BorderPane();
        layout.setPadding(new Insets(10));

        // Top section for inputs
        HBox inputSection = new HBox(10);
        inputSection.setPadding(new Insets(10));
        inputSection.getChildren().addAll(
                new Label("User ID:"), userIdComboBox,
                new Label("Salle ID:"), salleIdComboBox,
                new Label("Terrain ID:"), terrainIdComboBox,
                new Label("Date:"), datePicker,
                new Label("Start Time:"), startTimeField,
                new Label("End Time:"), endTimeField
        );

        // Button section
        HBox buttonSection = new HBox(10);
        buttonSection.setPadding(new Insets(10));
        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        buttonSection.getChildren().addAll(addButton, updateButton, deleteButton);

        // Set up table columns
        TableColumn<Reservation, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_reservation()).asObject());

        TableColumn<Reservation, Integer> userCol = new TableColumn<>("User ID");
        userCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_user()).asObject());

        TableColumn<Reservation, Integer> salleCol = new TableColumn<>("Salle ID");
        salleCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_salle()).asObject());

        TableColumn<Reservation, Integer> terrainCol = new TableColumn<>("Terrain ID");
        terrainCol.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getId_terrain()).asObject());

        TableColumn<Reservation, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate_reservation().toString()));

        TableColumn<Reservation, String> endDateCol = new TableColumn<>("End Date");
        endDateCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate_reservation_end().toString()));

        // Add the columns to the table
        reservationTable.getColumns().addAll(idCol, userCol, salleCol, terrainCol, dateCol, endDateCol);

        // Set the items in the table
        reservationTable.setItems(reservationList);

        // Add listeners to buttons
        addButton.setOnAction(e -> addReservation());
        updateButton.setOnAction(e -> updateReservation());
        deleteButton.setOnAction(e -> deleteReservation());

        VBox centerSection = new VBox(10, inputSection, buttonSection, reservationTable);
        layout.setCenter(centerSection);

        return layout;
    }

    private void loadReservationData() {
        try {
            List<Reservation> reservations = reservationDAO.getAll();
            reservationList.setAll(reservations);
        } catch (SQLException e) {
            showAlert("Error", "Failed to load reservations: " + e.getMessage());
        }
    }

    private void addReservation() {
        try {
            Reservation reservation = getReservationFromInputs();
            reservationDAO.add(reservation);
            reservationList.add(reservation);
        } catch (SQLException e) {
            showAlert("Error", "Failed to add reservation: " + e.getMessage());
        }
    }

    private void updateReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                Reservation updatedReservation = getReservationFromInputs();
                updatedReservation.setId_reservation(selectedReservation.getId_reservation());
                reservationDAO.update(updatedReservation);
                loadReservationData();
            } catch (SQLException e) {
                showAlert("Error", "Failed to update reservation: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "No reservation selected to update.");
        }
    }

    private void deleteReservation() {
        Reservation selectedReservation = reservationTable.getSelectionModel().getSelectedItem();
        if (selectedReservation != null) {
            try {
                reservationDAO.delete(selectedReservation.getId_reservation());
                reservationList.remove(selectedReservation);
            } catch (SQLException e) {
                showAlert("Error", "Failed to delete reservation: " + e.getMessage());
            }
        } else {
            showAlert("Warning", "No reservation selected to delete.");
        }
    }

    private Reservation getReservationFromInputs() {
        int userId = userIdComboBox.getValue();
        int salleId = salleIdComboBox.getValue();
        int terrainId = terrainIdComboBox.getValue();
        LocalDateTime startDateTime = LocalDateTime.parse(
                datePicker.getValue().toString() + " " + startTimeField.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );
        LocalDateTime endDateTime = LocalDateTime.parse(
                datePicker.getValue().toString() + " " + endTimeField.getText(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        );

        // Use the new constructor to initialize the Reservation object
        return new Reservation(startDateTime, endDateTime);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
