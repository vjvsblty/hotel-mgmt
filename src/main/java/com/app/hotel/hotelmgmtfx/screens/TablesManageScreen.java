package com.app.hotel.hotelmgmtfx.screens;

import com.app.hotel.hotelmgmtfx.model.HotelTable;
import com.app.hotel.hotelmgmtfx.utils.TableListFetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.List;

public class TablesManageScreen {

    private ObservableList<HotelTable> tablesList = FXCollections.observableArrayList(); // List to hold table data

    public void loadScreen(StackPane contentPane) {
        contentPane.getChildren().clear();

        VBox tablesManagePane = new VBox(20);
        tablesManagePane.setPadding(new Insets(20));
        tablesManagePane.setAlignment(Pos.TOP_CENTER);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search tables...");
        searchField.setStyle("-fx-font-size: 14px;");

        // Add Table Form
        TextField tableNumberField = new TextField();
        tableNumberField.setPromptText("Table Name");

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        Button addTableButton = new Button("Add Table");
        addTableButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        FlowPane tableCardsPane = new FlowPane(); // Updated for dynamic card layout
        tableCardsPane.setHgap(20);
        tableCardsPane.setVgap(20);
        tableCardsPane.setPadding(new Insets(10));
        tableCardsPane.setAlignment(Pos.CENTER);

        // Add new table to the database and update the UI
        addTableButton.setOnAction(e -> {
            String tableName = tableNumberField.getText();
            String capacity = capacityField.getText();

            if (!tableName.isEmpty() && !capacity.isEmpty()) {
                if (TableListFetcher.addTableToDatabase(tableName, capacity)) {
                    HotelTable newTable = new HotelTable(0, tableName, Integer.parseInt(capacity)); // Simulate new table
                    tablesList.add(newTable); // Add to ObservableList
                    updateTableCards(tableCardsPane, tablesList); // Refresh cards
                    tableNumberField.clear();
                    capacityField.clear();
                }
            }
        });

        HBox addTableBox = new HBox(10, tableNumberField, capacityField, addTableButton);
        addTableBox.setAlignment(Pos.CENTER);
        addTableBox.setPadding(new Insets(10));
        addTableBox.setStyle("-fx-border-color: #D5DBDB; -fx-border-radius: 5; -fx-background-color: #F9F9F9; "
                + "-fx-background-radius: 5; -fx-padding: 15;");

        // Fetch tables from the database and populate the ObservableList
        tablesList.addAll(TableListFetcher.fetchTablesFromDatabase());

        // Create a FilteredList for search functionality
        FilteredList<HotelTable> filteredTables = new FilteredList<>(tablesList, p -> true);

        // Update filter predicate based on search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredTables.setPredicate(table -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all tables if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return table.getTableName().toLowerCase().contains(lowerCaseFilter);
            });
            updateTableCards(tableCardsPane, filteredTables); // Refresh cards based on filtered list
        });

        // Initial population of cards
        updateTableCards(tableCardsPane, filteredTables);

        // Wrap the FlowPane (table cards) inside a ScrollPane for scrolling functionality
        ScrollPane scrollPane = new ScrollPane(tableCardsPane);
        scrollPane.setFitToWidth(true);  // Ensure the content stretches to fit the width of the pane
        scrollPane.setFitToHeight(false); // Allow vertical scrolling

        // Add components to the layout
        tablesManagePane.getChildren().addAll(searchField, addTableBox, scrollPane);
        contentPane.getChildren().add(tablesManagePane);
    }


    private void updateTableCards(FlowPane flowPane, List<HotelTable> tables) {
        flowPane.getChildren().clear(); // Clear current cards
        for (HotelTable table : tables) {
            addTableCard(flowPane, table);
        }
    }

    private void addTableCard(FlowPane flowPane, HotelTable table) {
        // Create a card
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: #D5DBDB; -fx-border-radius: 5; -fx-background-color: #F9F9F9; "
                + "-fx-background-radius: 5; -fx-padding: 10;");

        // Randomize the card color
        String[] colors = {"#FFDDC1", "#FFD1DC", "#D7BBF8", "#C8E6C9", "#FFCCBC"};
        String color = colors[(int) (Math.random() * colors.length)];
        card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Add table name and capacity
        Label tableNameLabel = new Label(table.getTableName());
        tableNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label capacityLabel = new Label("Capacity: " + table.getTableSize());
        capacityLabel.setStyle("-fx-font-size: 14px;");

        // Edit button
        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white;");
        editButton.setOnAction(e -> {
            showEditDialog(table, flowPane);
        });

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> {
            showDeleteDialog(table, flowPane, tablesList);
        });

        // Add components to the card
        card.getChildren().addAll(tableNameLabel, capacityLabel, editButton, deleteButton);

        // Add the card to the FlowPane
        flowPane.getChildren().add(card);
    }

    private void showEditDialog(HotelTable table, FlowPane flowPane) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Table");

        VBox editPane = new VBox(20);
        editPane.setAlignment(Pos.CENTER);
        editPane.setPadding(new Insets(20));

        TextField tableNameField = new TextField(table.getTableName());
        tableNameField.setPromptText("Table Name");

        TextField capacityField = new TextField(String.valueOf(table.getTableSize()));
        capacityField.setPromptText("Capacity");

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String newTableName = tableNameField.getText();
            String newCapacity = capacityField.getText();

            if (!newTableName.isEmpty() && !newCapacity.isEmpty()) {
                try {
                    // Update the table object
                    table.setTableName(newTableName);
                    table.setTableSize(Integer.parseInt(newCapacity));

                    // Update in database
                    TableListFetcher.updateTableInDatabase(table);

                    // Refresh the FlowPane
                    updateTableCards(flowPane, tablesList);

                    editStage.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    showErrorDialog("Failed to update the table. Please try again.");
                }
            } else {
                showErrorDialog("Please fill in all fields.");
            }
        });

        editPane.getChildren().addAll(new Label("Edit Table Details:"), tableNameField, capacityField, saveButton);

        Scene scene = new Scene(editPane, 300, 200);
        editStage.setScene(scene);
        editStage.show();
    }

    private void showDeleteDialog(HotelTable table, FlowPane flowPane, List<HotelTable> tablesList) {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete Table");

        VBox deletePane = new VBox(20);
        deletePane.setAlignment(Pos.CENTER);
        deletePane.setPadding(new Insets(20));

        Label messageLabel = new Label("Are you sure you want to delete: "+table.getTableName()+" ?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        noButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        // Action when "Yes" is clicked
        yesButton.setOnAction(e -> {
            try {
                // Delete the table from the database
                TableListFetcher.deleteTableFromDatabase(table);

                // Refresh the list of tables from the database (get the latest list)
                tablesList.clear();  // Clear the old list
                tablesList.addAll(TableListFetcher.fetchTablesFromDatabase());  // Add the updated list

                // Update the FlowPane or TableView with the latest tables list
                updateTableCards(flowPane, tablesList);

                // Close the delete dialog
                deleteStage.close();

            } catch (SQLException ex) {
                showErrorDialog("Failed to delete the table. Please try again.");
            }
        });

        // Action when "No" is clicked (close the dialog)
        noButton.setOnAction(e -> deleteStage.close());

        deletePane.getChildren().addAll(messageLabel, yesButton, noButton);

        Scene scene = new Scene(deletePane, 300, 200);
        deleteStage.setScene(scene);
        deleteStage.show();
    }



    private void showErrorDialog(String message) {
        Stage errorStage = new Stage();
        errorStage.setTitle("Error");

        VBox errorPane = new VBox(20);
        errorPane.setAlignment(Pos.CENTER);
        errorPane.setPadding(new Insets(20));

        Label errorMessage = new Label(message);
        errorMessage.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");

        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> errorStage.close());

        errorPane.getChildren().addAll(errorMessage, closeButton);

        Scene scene = new Scene(errorPane, 300, 150);
        errorStage.setScene(scene);
        errorStage.show();
    }
}
