package com.example.javarendu.Pages;

import com.example.javarendu.App;
import com.example.javarendu.dao.EvenementDAO;
import com.example.javarendu.model.Evenement;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.beans.property.SimpleStringProperty;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class EvenementPage {
    private BorderPane layout;
    private TableView<Evenement> table;
    private EvenementDAO evenementDAO;
    private DashboardPage dashboardPage;

    public EvenementPage(DashboardPage dashboardPage) throws SQLException {
        this.dashboardPage = dashboardPage;  // Initialize the reference
        evenementDAO = new EvenementDAO();

        layout = new BorderPane();

        // Title
        Label title = new Label("Evenements");
        title.setFont(Font.font("Arial", 24));
        title.getStyleClass().add("page-title");

        // Table setup
        table = new TableView<>();
        TableColumn<Evenement, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        TableColumn<Evenement, String> descriptionColumn = new TableColumn<>("Description");
        descriptionColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDescription()));

        TableColumn<Evenement, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType().name()));

        TableColumn<Evenement, String> dateDebutColumn = new TableColumn<>("Date Debut");
        dateDebutColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateDebut().toString()));

        TableColumn<Evenement, String> dateFinColumn = new TableColumn<>("Date Fin");
        dateFinColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDateFin().toString()));

        table.getColumns().addAll(nomColumn, descriptionColumn, typeColumn, dateDebutColumn, dateFinColumn);

        // Fetch evenements and display
        loadEvenements();

        // Create form and buttons
        VBox form = createForm();
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteEvenement());

        HBox buttonBox = new HBox(10, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Combine all components into one layout
        VBox content = new VBox(10, title, table, form, buttonBox);
        layout.setCenter(content);
    }

    public BorderPane getLayout() {
        return layout;
    }

    // Load evenements into the table
    private void loadEvenements() {
        try {
            List<Evenement> evenements = evenementDAO.getAll();
            table.getItems().setAll(evenements);
        } catch (Exception e) {
            showAlert("Error loading evenements: " + e.getMessage());
        }
    }

    // Form for adding or updating an evenement
    private VBox createForm() {
        Label formTitle = new Label("Add/Update Evenement");
        formTitle.setFont(Font.font("Arial", 18));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Description");

        TextField dateDebutField = new TextField();
        dateDebutField.setPromptText("Date Debut (yyyy-mm-dd hh:mm)");

        TextField dateFinField = new TextField();
        dateFinField.setPromptText("Date Fin (yyyy-mm-dd hh:mm)");

        ComboBox<Evenement.TypeEvenement> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll(Evenement.TypeEvenement.values());
        typeComboBox.setPromptText("Select Type");

        Button saveButton = new Button("Save Evenement");
        saveButton.setOnAction(e -> saveEvenement(nomField, descriptionField, dateDebutField, dateFinField, typeComboBox));

        VBox form = new VBox(10, formTitle, nomField, descriptionField, dateDebutField, dateFinField, typeComboBox, saveButton);
        form.setAlignment(Pos.CENTER);
        return form;
    }

    // Save evenement to DB
    private void saveEvenement(TextField nomField, TextField descriptionField, TextField dateDebutField, TextField dateFinField, ComboBox<Evenement.TypeEvenement> typeComboBox) {
        String nom = nomField.getText();
        String description = descriptionField.getText();
        String dateDebut = dateDebutField.getText();
        String dateFin = dateFinField.getText();
        Evenement.TypeEvenement type = typeComboBox.getValue();

        if (nom.isEmpty() || description.isEmpty() || dateDebut.isEmpty() || dateFin.isEmpty() || type == null) {
            showAlert("Please fill in all fields!");
            return;
        }

        try {
            Evenement evenement = new Evenement(
                    1, // Assuming user ID is 1 for now
                    nom,
                    description,
                    LocalDateTime.parse(dateDebut),
                    LocalDateTime.parse(dateFin),
                    type
            );
            evenementDAO.add(evenement);  // Add to DB
            loadEvenements();  // Refresh table
            showAlert("Evenement saved successfully!");

            // Clear form
            nomField.clear();
            descriptionField.clear();
            dateDebutField.clear();
            dateFinField.clear();
            typeComboBox.getSelectionModel().clearSelection();
        } catch (Exception e) {
            showAlert("Error saving evenement: " + e.getMessage());
        }
    }

    // Delete selected evenement
    private void deleteEvenement() {
        Evenement selectedEvenement = table.getSelectionModel().getSelectedItem();
        if (selectedEvenement != null) {
            try {
                evenementDAO.delete(selectedEvenement.getId());
                loadEvenements();  // Refresh table
                showAlert("Evenement deleted successfully!");
            } catch (Exception e) {
                showAlert("Error deleting evenement: " + e.getMessage());
            }
        } else {
            showAlert("Please select an evenement to delete.");
        }
    }

    // Show alert dialog
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
