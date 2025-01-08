package com.example.javarendu.Pages;

import com.example.javarendu.dao.UtilisateurDAO;
import com.example.javarendu.model.Utilisateur;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.beans.property.SimpleStringProperty;

import java.util.List;

public class UtilisateurPage {
    private BorderPane layout;
    private TableView<Utilisateur> table;
    private UtilisateurDAO utilisateurDAO;

    public UtilisateurPage(DashboardPage dashboardPage) {
        utilisateurDAO = new UtilisateurDAO();

        layout = new BorderPane();

        // Title
        Label title = new Label("Utilisateurs");
        title.setFont(Font.font("Arial", 24));
        title.getStyleClass().add("page-title");

        // Table setup
        table = new TableView<>();
        TableColumn<Utilisateur, String> nomColumn = new TableColumn<>("Nom");
        nomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNom()));

        TableColumn<Utilisateur, String> prenomColumn = new TableColumn<>("Prenom");
        prenomColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getPrenom()));

        TableColumn<Utilisateur, String> emailColumn = new TableColumn<>("Email");
        emailColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        TableColumn<Utilisateur, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getType()));

        table.getColumns().addAll(nomColumn, prenomColumn, emailColumn, typeColumn);

        // Fetch utilisateurs and display
        loadUtilisateurs();

        // Create form and buttons
        VBox form = createForm();
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteUtilisateur());

        HBox buttonBox = new HBox(10, deleteButton);
        buttonBox.setAlignment(Pos.CENTER);

        // Combine all components into one layout
        VBox content = new VBox(10, title, table, form, buttonBox);
        layout.setCenter(content);
    }

    public BorderPane getLayout() {
        return layout;
    }

    // Load utilisateurs into the table
    private void loadUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurDAO.getAll();
        table.getItems().setAll(utilisateurs);
    }

    // Form for adding or updating an utilisateur
    private VBox createForm() {
        Label formTitle = new Label("Add/Update Utilisateur");
        formTitle.setFont(Font.font("Arial", 18));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextField prenomField = new TextField();
        prenomField.setPromptText("Prenom");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("ETUDIANT", "PROF");
        typeComboBox.setPromptText("Select Type");

        Button saveButton = new Button("Save Utilisateur");
        saveButton.setOnAction(e -> saveUtilisateur(nomField, prenomField, emailField, typeComboBox));

        VBox form = new VBox(10, formTitle, nomField, prenomField, emailField, typeComboBox, saveButton);
        form.setAlignment(Pos.CENTER);
        return form;
    }

    // Save utilisateur to DB
    private void saveUtilisateur(TextField nomField, TextField prenomField, TextField emailField, ComboBox<String> typeComboBox) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String email = emailField.getText();
        String type = typeComboBox.getValue();

        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty() || type == null) {
            showAlert("Please fill in all fields!");
            return;
        }

        Utilisateur utilisateur = new Utilisateur(nom, prenom, email, type);
        utilisateurDAO.add(utilisateur);  // Add to DB
        loadUtilisateurs();  // Refresh table
        showAlert("Utilisateur saved successfully!");

        // Clear form
        nomField.clear();
        prenomField.clear();
        emailField.clear();
        typeComboBox.getSelectionModel().clearSelection();
    }

    // Delete selected utilisateur
    private void deleteUtilisateur() {
        Utilisateur selectedUtilisateur = table.getSelectionModel().getSelectedItem();
        if (selectedUtilisateur != null) {
            utilisateurDAO.delete(selectedUtilisateur.getIdUtilisateur());
            loadUtilisateurs();  // Refresh table
            showAlert("Utilisateur deleted successfully!");
        } else {
            showAlert("Please select an utilisateur to delete.");
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
