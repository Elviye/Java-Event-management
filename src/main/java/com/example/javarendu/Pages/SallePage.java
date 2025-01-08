package com.example.javarendu.Pages;

import com.example.javarendu.dao.SalleDAO;
import com.example.javarendu.model.Salle;
import com.example.javarendu.model.Salle.TypeSalle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class SallePage {
    private BorderPane layout;

    public SallePage() {
        layout = new BorderPane();
        TableView<?> tableView = new TableView<>();
        Button addButton = new Button("Add Salle");

        VBox vbox = new VBox(10, addButton, tableView);
        layout.setCenter(vbox);
    }

    public BorderPane getLayout() {
        return layout;
    }

    private final SalleDAO salleDAO = new SalleDAO();
    private final TableView<Salle> tableView = new TableView<>();
    private final ObservableList<Salle> salleList = FXCollections.observableArrayList();

    public void show(Stage stage) {
        VBox root = new VBox(10);
        root.setPadding(new Insets(10));

        // TableView columns
        TableColumn<Salle, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getId()).asObject());

        TableColumn<Salle, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNom()));

        TableColumn<Salle, String> nomCompletCol = new TableColumn<>("Nom Complet");
        nomCompletCol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getNomComplet()));

        TableColumn<Salle, Integer> capaciteCol = new TableColumn<>("Capacité");
        capaciteCol.setCellValueFactory(data -> new javafx.beans.property.SimpleIntegerProperty(data.getValue().getCapacite()).asObject());

        TableColumn<Salle, TypeSalle> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getType()));

        TableColumn<Salle, Boolean> dispoCol = new TableColumn<>("Disponible");
        dispoCol.setCellValueFactory(data -> new javafx.beans.property.SimpleBooleanProperty(data.getValue().isEstDisponible()).asObject());

        tableView.getColumns().addAll(idCol, nomCol, nomCompletCol, capaciteCol, typeCol, dispoCol);
        tableView.setItems(salleList);

        // Form fields
        TextField nomField = new TextField();
        TextField nomCompletField = new TextField();
        TextField capaciteField = new TextField();
        ComboBox<TypeSalle> typeComboBox = new ComboBox<>(FXCollections.observableArrayList(TypeSalle.values()));
        CheckBox dispoCheckBox = new CheckBox("Disponible");

        Button addButton = new Button("Add");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");

        // Form layout
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.addRow(0, new Label("Nom:"), nomField);
        form.addRow(1, new Label("Nom Complet:"), nomCompletField);
        form.addRow(2, new Label("Capacité:"), capaciteField);
        form.addRow(3, new Label("Type:"), typeComboBox);
        form.addRow(4, new Label("Disponible:"), dispoCheckBox);
        form.addRow(5, addButton, updateButton, deleteButton);

        // Populate form when selecting a row
        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                nomField.setText(newSelection.getNom());
                nomCompletField.setText(newSelection.getNomComplet());
                capaciteField.setText(String.valueOf(newSelection.getCapacite()));
                typeComboBox.setValue(newSelection.getType());
                dispoCheckBox.setSelected(newSelection.isEstDisponible());
            }
        });

        // Button actions
        addButton.setOnAction(e -> {
            try {
                Salle salle = new Salle(
                        nomField.getText(),
                        nomCompletField.getText(),
                        Integer.parseInt(capaciteField.getText()),
                        typeComboBox.getValue()
                );
                salle.setEstDisponible(dispoCheckBox.isSelected());
                salleDAO.add(salle);
                refreshTable();
                clearForm(nomField, nomCompletField, capaciteField, typeComboBox, dispoCheckBox);
            } catch (SQLException ex) {
                showError("Error adding salle: " + ex.getMessage());
            } catch (NumberFormatException ex) {
                showError("Capacité must be a number.");
            }
        });

        updateButton.setOnAction(e -> {
            Salle selectedSalle = tableView.getSelectionModel().getSelectedItem();
            if (selectedSalle != null) {
                try {
                    selectedSalle.setNom(nomField.getText());
                    selectedSalle.setNomComplet(nomCompletField.getText());
                    selectedSalle.setCapacite(Integer.parseInt(capaciteField.getText()));
                    selectedSalle.setType(typeComboBox.getValue());
                    selectedSalle.setEstDisponible(dispoCheckBox.isSelected());
                    salleDAO.update(selectedSalle);
                    refreshTable();
                    clearForm(nomField, nomCompletField, capaciteField, typeComboBox, dispoCheckBox);
                } catch (SQLException ex) {
                    showError("Error updating salle: " + ex.getMessage());
                }
            }
        });

        deleteButton.setOnAction(e -> {
            Salle selectedSalle = tableView.getSelectionModel().getSelectedItem();
            if (selectedSalle != null) {
                try {
                    salleDAO.delete(selectedSalle.getId());
                    refreshTable();
                } catch (SQLException ex) {
                    showError("Error deleting salle: " + ex.getMessage());
                }
            }
        });

        // Load data and set scene
        refreshTable();
        root.getChildren().addAll(tableView, form);
        stage.setScene(new Scene(root, 800, 600));
        stage.setTitle("Gestion des Salles");
        stage.show();
    }


    private void refreshTable() {
        salleList.clear();
        try {
            List<Salle> salles = salleDAO.getAll();
            salleList.addAll(salles);
        } catch (SQLException ex) {
            showError("Error loading salles: " + ex.getMessage());
        }
    }

    private void clearForm(TextField nomField, TextField nomCompletField, TextField capaciteField,
                           ComboBox<TypeSalle> typeComboBox, CheckBox dispoCheckBox) {
        nomField.clear();
        nomCompletField.clear();
        capaciteField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        dispoCheckBox.setSelected(false);
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
