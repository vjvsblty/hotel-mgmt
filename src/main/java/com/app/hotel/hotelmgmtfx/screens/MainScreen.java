package com.app.hotel.hotelmgmtfx.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainScreen {
    private Button selectedButton = null; // Track the currently selected button

    public void showMainScreen(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();

        // Header Section
        HBox header = new HBox(10);
        header.setPadding(new Insets(10));
        header.setStyle("-fx-background-color: #2C3E50;");
        header.setAlignment(Pos.CENTER);

        // Hotel Name
        Label hotelNameLabel = new Label("हॉटेल चुलांगन");
        hotelNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #ECF0F1;");

        // Add hotel name to the header
        header.getChildren().addAll(hotelNameLabel);

        // Side Menu
        VBox sideMenu = new VBox(10);
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #2C3E50;");
        sideMenu.setPrefWidth(250); // Slightly wider side menu for better spacing

        // Menu Buttons
        Button menuCardButton = new Button("Menu Card");
        Button tablesManageButton = new Button("Tables");
        Button manageOrdersButton = new Button("Orders/Bills");

        // Apply styles to the buttons
        styleSideMenuButton(menuCardButton);
        styleSideMenuButton(tablesManageButton);
        styleSideMenuButton(manageOrdersButton);

        // Add buttons to the side menu
        sideMenu.getChildren().addAll(menuCardButton, tablesManageButton, manageOrdersButton);
        mainLayout.setLeft(sideMenu);

        // Center content area
        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        mainLayout.setCenter(contentPane);
        mainLayout.setTop(header);

        menuCardButton.setOnAction(e -> {
            new MenuCardScreen().loadScreen(contentPane);
            highlightSelectedButton(menuCardButton, tablesManageButton, manageOrdersButton);
        });

        tablesManageButton.setOnAction(e -> {
            new TablesManageScreen().loadScreen(contentPane);
            highlightSelectedButton(tablesManageButton, menuCardButton, manageOrdersButton);
        });

        manageOrdersButton.setOnAction(e -> {
            new ManageOrdersScreen().loadScreen(contentPane);
            highlightSelectedButton(manageOrdersButton, menuCardButton, tablesManageButton);
        });

        // Create and set scene
        Scene mainScene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true); // Maximized window
        primaryStage.setFullScreen(true); // Optional: Full-screen mode
        primaryStage.setTitle("Hotel Management - Home");
    }

    // Styling for Side Menu Buttons
    private void styleSideMenuButton(Button button) {
        button.setMaxWidth(Double.MAX_VALUE);  // Make button take up full width
        button.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");

        button.setOnMouseEntered(e -> {
            if (button != selectedButton) {
                button.setStyle("-fx-background-color: #1ABC9C; -fx-text-fill: white; "
                        + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
            }
        });

        button.setOnMouseExited(e -> {
            if (button != selectedButton) {
                button.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; "
                        + "-fx-font-size: 16px; -fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
            }
        });
    }

    private void highlightSelectedButton(Button newSelectedButton, Button... otherButtons) {
        // Reset the previously selected button, if any
        if (selectedButton != null) {
            resetButtonStyle(selectedButton);
        }

        // Highlight the new selected button
        selectedButton = newSelectedButton;
        selectedButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-size: 16px; "
                + "-fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");

        // Reset styles of other buttons
        for (Button button : otherButtons) {
            resetButtonStyle(button);
        }
    }

    private void resetButtonStyle(Button button) {
        if (button != selectedButton) { // Only reset if not the currently selected button
            button.setStyle("-fx-background-color: #34495E; -fx-text-fill: #ECF0F1; -fx-font-size: 16px; "
                    + "-fx-font-weight: bold; -fx-border-radius: 5; -fx-background-radius: 5;");
        }
    }



}
