package com.app.hotel.hotelmgmtfx.screens;
import com.app.hotel.hotelmgmtfx.db.DatabaseConnection;
import com.app.hotel.hotelmgmtfx.utils.MenuItemFetcher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import com.app.hotel.hotelmgmtfx.model.MenuItem;
import javafx.stage.Stage;

public class MenuCardScreen {

    private ObservableList<MenuItem> menuItemsList = FXCollections.observableArrayList();

    public void loadScreen(StackPane contentPane) {
        contentPane.getChildren().clear();

        VBox menuManagePane = new VBox(20);
        menuManagePane.setPadding(new Insets(20));
        menuManagePane.setAlignment(Pos.TOP_CENTER);

        // Search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search items...");
        searchField.setStyle("-fx-font-size: 14px;");

        // Add Item Form
        TextField itemNameField = new TextField();
        itemNameField.setPromptText("Item Name");

        TextField itemPriceField = new TextField();
        itemPriceField.setPromptText("Price");

        Button addItemButton = new Button("Add Item");
        addItemButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        FlowPane menuCardsPane = new FlowPane();
        menuCardsPane.setHgap(20);
        menuCardsPane.setVgap(20);
        menuCardsPane.setPadding(new Insets(10));
        menuCardsPane.setAlignment(Pos.CENTER);

        // Add new item to the database and update the UI
        addItemButton.setOnAction(e -> {
            String itemName = itemNameField.getText();
            String itemPrice = itemPriceField.getText();

            if (!itemName.isEmpty() && !itemPrice.isEmpty()) {
                addItemToDatabase(itemName, itemPrice); // Add to DB
                menuItemsList.add(new MenuItem(itemName, Double.parseDouble(itemPrice))); // Update the ObservableList
                updateMenuCards(menuCardsPane, menuItemsList);
                itemNameField.clear();
                itemPriceField.clear();
            }
        });

        HBox addItemBox = new HBox(10, itemNameField, itemPriceField, addItemButton);
        addItemBox.setAlignment(Pos.CENTER);
        addItemBox.setPadding(new Insets(10));
        addItemBox.setStyle("-fx-border-color: #D5DBDB; -fx-border-radius: 5; -fx-background-color: #F9F9F9; "
                + "-fx-background-radius: 5; -fx-padding: 15;");

        // Fetch menu items from the database
        menuItemsList.addAll(MenuItemFetcher.fetchMenuItems());

        // Create a FilteredList for search functionality
        FilteredList<MenuItem> filteredItems = new FilteredList<>(menuItemsList, p -> true);

        // Update filter predicate based on search field
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredItems.setPredicate(item -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // Show all items if search is empty
                }
                String lowerCaseFilter = newValue.toLowerCase();
                return item.getName().toLowerCase().contains(lowerCaseFilter);
            });
            updateMenuCards(menuCardsPane, filteredItems);
        });

        // Initial population of cards
        updateMenuCards(menuCardsPane, filteredItems);

        // Wrap the FlowPane with the menu items inside the ScrollPane for scrolling
        ScrollPane scrollPane = new ScrollPane(menuCardsPane);
        scrollPane.setFitToWidth(true);  // Ensures the content stretches to fit the width of the pane
        scrollPane.setFitToHeight(false); // Allow vertical scrolling but not full height fit

        // Add components to the layout
        menuManagePane.getChildren().addAll(searchField, addItemBox, scrollPane);
        contentPane.getChildren().add(menuManagePane);
    }


    private void updateMenuCards(FlowPane flowPane, List<MenuItem> items) {
        flowPane.getChildren().clear();
        for (MenuItem item : items) {
            addMenuCard(flowPane, item);
        }
    }

    private void addMenuCard(FlowPane flowPane, MenuItem item) {
        flowPane.setHgap(10); // Horizontal gap between cards
        flowPane.setVgap(10); // Vertical gap between rows
        flowPane.setPrefWrapLength(4 * 200 + 30); // 4 cards + gaps

        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(10));
        card.setPrefWidth(200); // Set fixed width
        card.setPrefHeight(150); // Set fixed height
        card.setStyle("-fx-border-color: #D5DBDB; -fx-border-radius: 5; -fx-background-color: #F9F9F9; "
                + "-fx-background-radius: 5; -fx-padding: 10;");

        String[] colors = {"#FFDDC1", "#FFD1DC", "#D7BBF8", "#C8E6C9", "#FFCCBC"};
        String color = colors[(int) (Math.random() * colors.length)];
        card.setStyle("-fx-background-color: " + color + "; -fx-border-radius: 5; -fx-background-radius: 5;");

        Label itemNameLabel = new Label(item.getName());
        itemNameLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        Label itemPriceLabel = new Label("Price: ₹" + item.getPrice());
        itemPriceLabel.setStyle("-fx-font-size: 14px;");

        Button editButton = new Button("Edit");
        editButton.setStyle("-fx-background-color: #F39C12; -fx-text-fill: white;");
        editButton.setOnAction(e -> showEditDialog(item, flowPane));

        Button deleteButton = new Button("Delete");
        deleteButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        deleteButton.setOnAction(e -> showDeleteDialog(item, flowPane, menuItemsList));

        card.getChildren().addAll(itemNameLabel, itemPriceLabel, editButton, deleteButton);
        flowPane.getChildren().add(card);
    }

    private void showEditDialog(MenuItem item, FlowPane flowPane) {
        Stage editStage = new Stage();
        editStage.setTitle("Edit Menu Item");

        VBox editPane = new VBox(20);
        editPane.setAlignment(Pos.CENTER);
        editPane.setPadding(new Insets(20));

        TextField nameField = new TextField(item.getName());
        TextField priceField = new TextField(String.valueOf(item.getPrice()));

        Button saveButton = new Button("Save");
        saveButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveButton.setOnAction(e -> {
            String updatedName = nameField.getText();
            String updatedPrice = priceField.getText();

            if (!updatedName.isEmpty() && !updatedPrice.isEmpty()) {
                item.setName(updatedName);
                item.setPrice(Double.parseDouble(updatedPrice));

                updateItemInDatabase(item, updatedName, updatedPrice); // Update in database
                updateMenuCards(flowPane, menuItemsList); // Refresh cards
                editStage.close();
            }
        });

        editPane.getChildren().addAll(new Label("Edit Menu Item:"), nameField, priceField, saveButton);

        Scene scene = new Scene(editPane, 300, 200);
        editStage.setScene(scene);
        editStage.show();
    }

    private void showDeleteDialog(MenuItem item, FlowPane flowPane, List<MenuItem> menuItemsList) {
        Stage deleteStage = new Stage();
        deleteStage.setTitle("Delete Menu Item");

        VBox deletePane = new VBox(20);
        deletePane.setAlignment(Pos.CENTER);
        deletePane.setPadding(new Insets(20));

        Label messageLabel = new Label("Are you sure you want to delete: "+item.getName()+" ?");
        Button yesButton = new Button("Yes");
        Button noButton = new Button("No");

        yesButton.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        noButton.setStyle("-fx-background-color: gray; -fx-text-fill: white;");

        // Action when "Yes" is clicked
        yesButton.setOnAction(e -> {
            try {
                // Delete the menu item from the database
                MenuItemFetcher.deleteItemFromDatabase(item);

                // Remove the item from the menuItemsList
                menuItemsList.remove(item);

                // Refresh the FlowPane with the updated menu items list
                updateMenuCards(flowPane, menuItemsList);

                // Close the delete dialog
                deleteStage.close();

            } catch (SQLException ex) {
            }
        });

        // Action when "No" is clicked (close the dialog)
        noButton.setOnAction(e -> deleteStage.close());

        deletePane.getChildren().addAll(messageLabel, yesButton, noButton);

        Scene scene = new Scene(deletePane, 300, 200);
        deleteStage.setScene(scene);
        deleteStage.show();
    }



    // Update item in the database
    private void updateItemInDatabase(MenuItem menuItem, String name, String price) {
        String query = "UPDATE menu_items SET name = ?, price = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.setInt(3, menuItem.getId()); // Assuming `MenuItem` has an `id` field
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Add item to the database
    private void addItemToDatabase(String name, String price) {
        String query = "INSERT INTO menu_items (name, price) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, name);
            stmt.setDouble(2, Double.parseDouble(price));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
