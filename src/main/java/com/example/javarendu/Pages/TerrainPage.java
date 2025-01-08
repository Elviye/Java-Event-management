package com.example.javarendu.Pages;

import com.example.javarendu.dao.TerrainDAO;
import com.example.javarendu.model.Terrain;
import com.example.javarendu.model.Terrain.TypeTerrain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class TerrainPage {

    private final Connection connection;
    private final TerrainDAO terrainDAO;
    private TableView<Terrain> tableView;
    private ObservableList<Terrain> terrainList;

    public TerrainPage(Connection connection) {
        this.connection = connection;
        this.terrainDAO = new TerrainDAO(connection);
    }

    public void show(Stage primaryStage) {
        primaryStage.setTitle("Gestion des Terrains");

        // Create the table view and columns
        tableView = new TableView<>();
        TableColumn<Terrain, String> nomCol = new TableColumn<>("Nom");
        nomCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));

        TableColumn<Terrain, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().name()));

        TableColumn<Terrain, Integer> capaciteCol = new TableColumn<>("Capacité");
        capaciteCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getCapacite()).asObject());

        TableColumn<Terrain, Boolean> couvertCol = new TableColumn<>("Couverte");
        couvertCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isEstCouverte()).asObject());

        TableColumn<Terrain, Boolean> dispoCol = new TableColumn<>("Disponible");
        dispoCol.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().isEstDisponible()).asObject());

        tableView.getColumns().addAll(nomCol, typeCol, capaciteCol, couvertCol, dispoCol);

        // Load data from the database
        loadTerrainData();

        // Form for adding/editing terrains
        VBox formBox = createForm();

        // Layout
        BorderPane layout = new BorderPane();
        layout.setCenter(tableView);
        layout.setRight(formBox);
        layout.setPadding(new Insets(10));

        Scene scene = new Scene(layout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createForm() {
        VBox formBox = new VBox(10);
        formBox.setPadding(new Insets(10));

        TextField nomField = new TextField();
        nomField.setPromptText("Nom");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Description");

        ComboBox<TypeTerrain> typeComboBox = new ComboBox<>();
        typeComboBox.setItems(FXCollections.observableArrayList(TypeTerrain.values()));

        Spinner<Integer> capaciteSpinner = new Spinner<>(1, 1000, 10);
        capaciteSpinner.setEditable(true);

        CheckBox couvertCheckBox = new CheckBox("Couverte");
        CheckBox dispoCheckBox = new CheckBox("Disponible");

        Button addButton = new Button("Ajouter");
        addButton.setOnAction(e -> {
            try {
                Terrain terrain = new Terrain(
                        nomField.getText(),
                        descriptionField.getText(),
                        typeComboBox.getValue(),
                        capaciteSpinner.getValue(),
                        couvertCheckBox.isSelected()
                );
                terrain.setEstDisponible(dispoCheckBox.isSelected());
                terrainDAO.add(terrain);
                loadTerrainData();
                clearForm(nomField, descriptionField, typeComboBox, capaciteSpinner, couvertCheckBox, dispoCheckBox);
            } catch (SQLException ex) {
                showAlert("Erreur", "Impossible d'ajouter le terrain : " + ex.getMessage());
            }
        });

        formBox.getChildren().addAll(
                new Label("Nom:"), nomField,
                new Label("Description:"), descriptionField,
                new Label("Type:"), typeComboBox,
                new Label("Capacité:"), capaciteSpinner,
                couvertCheckBox, dispoCheckBox,
                addButton
        );

        return formBox;
    }

    private void loadTerrainData() {
        try {
            List<Terrain> terrains = terrainDAO.getAll();
            terrainList = FXCollections.observableArrayList(terrains);
            tableView.setItems(terrainList);
        } catch (SQLException ex) {
            showAlert("Erreur", "Impossible de charger les terrains : " + ex.getMessage());
        }
    }

    private void clearForm(TextField nomField, TextArea descriptionField,
                           ComboBox<TypeTerrain> typeComboBox, Spinner<Integer> capaciteSpinner,
                           CheckBox couvertCheckBox, CheckBox dispoCheckBox) {
        nomField.clear();
        descriptionField.clear();
        typeComboBox.getSelectionModel().clearSelection();
        capaciteSpinner.getValueFactory().setValue(10);
        couvertCheckBox.setSelected(false);
        dispoCheckBox.setSelected(false);
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
