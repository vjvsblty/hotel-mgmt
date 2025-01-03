package com.app.hotel.hotelmgmtfx.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainScreen {
    private Button selectedButton = null; // Track the currently selected button

    public void showMainScreen(Stage primaryStage) {
        BorderPane mainLayout = new BorderPane();

        // Side Menu
        VBox sideMenu = new VBox(20); // Add spacing between elements
        sideMenu.setPadding(new Insets(20));
        sideMenu.setStyle("-fx-background-color: #2C3E50;");
        sideMenu.setPrefWidth(250); // Fixed width for the sidebar
        sideMenu.setAlignment(Pos.TOP_CENTER); // Align content to the center

        // Logo
        ImageView logo = new ImageView(new Image("file:src/main/resources/hotel-gate.jpg")); // Replace with the actual path to the logo
        logo.setFitWidth(200);
        logo.setFitHeight(200);

        // Hotel Name
        Label hotelNameLabel = new Label("हॉटेल चुलांगण");
        hotelNameLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #FFD700;");
        hotelNameLabel.setWrapText(true);
        hotelNameLabel.setAlignment(Pos.CENTER);

        // Menu Buttons
        Button menuCardButton = new Button("Menu Card");
        Button tablesManageButton = new Button("Tables");
        Button manageOrdersButton = new Button("Orders/Bills");
        Button earningButton = new Button("Earning");
        Button cashFlowButton = new Button("CashFlow");

        styleSideMenuButton(menuCardButton);
        styleSideMenuButton(tablesManageButton);
        styleSideMenuButton(manageOrdersButton);
        styleSideMenuButton(earningButton);
        styleSideMenuButton(cashFlowButton);

        // Add elements to the side menu
        sideMenu.getChildren().addAll(logo, hotelNameLabel, menuCardButton, tablesManageButton, manageOrdersButton, earningButton, cashFlowButton);
        sideMenu.setAlignment(Pos.CENTER); // Center all items vertically

        // Fill the remaining space at the bottom
        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);
        sideMenu.getChildren().add(spacer);

        // Center content area
        StackPane contentPane = new StackPane();
        contentPane.setPadding(new Insets(20));
        mainLayout.setCenter(contentPane);
        mainLayout.setLeft(sideMenu);

        // Button actions
        menuCardButton.setOnAction(e -> {
            new MenuCardScreen().loadScreen(contentPane);
            highlightSelectedButton(menuCardButton, tablesManageButton, manageOrdersButton, earningButton, cashFlowButton);
        });
        tablesManageButton.setOnAction(e -> {
            new TablesManageScreen().loadScreen(contentPane);
            highlightSelectedButton(tablesManageButton, menuCardButton, manageOrdersButton, earningButton, cashFlowButton);
        });
        manageOrdersButton.setOnAction(e -> {
            new ManageOrdersScreen().loadScreen(contentPane);
            highlightSelectedButton(manageOrdersButton, menuCardButton, tablesManageButton, earningButton, cashFlowButton);
        });
        earningButton.setOnAction(e -> {
            new EarningScreen().loadScreen(contentPane);
            highlightSelectedButton(earningButton, menuCardButton, tablesManageButton, manageOrdersButton, cashFlowButton);
        });
        cashFlowButton.setOnAction(e -> {
            new CashFlowScreen().loadScreen(contentPane);
            highlightSelectedButton(cashFlowButton, earningButton, menuCardButton, tablesManageButton, manageOrdersButton);
        });

        // Scene setup
        Scene mainScene = new Scene(mainLayout, 1024, 768);
        primaryStage.setScene(mainScene);
        primaryStage.setMaximized(true); // Make the window full screen
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
