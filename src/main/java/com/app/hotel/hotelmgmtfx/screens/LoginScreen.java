package com.app.hotel.hotelmgmtfx.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginScreen {

    private static final String USERNAME = "admin";
    private static final String PASSWORD = "admin";

    public void showLoginScreen(Stage primaryStage) {
        primaryStage.setTitle("Login Page");

        // Root Layout: StackPane to stack background image and login form
        StackPane root = new StackPane();

        // Background: Logo as Background Image
        ImageView logoImageView = new ImageView(new Image("file:src/main/resources/hotel-menu.jpg")); // Your logo path
        logoImageView.setFitWidth(900); // Adjust width as necessary
        logoImageView.setFitHeight(900); // Adjust height as necessary
        logoImageView.setPreserveRatio(true);

        // Add the logo image as the first layer in the stack
        root.getChildren().add(logoImageView);

        // Card Layout for the Login Form (centered on top of the logo)
        VBox card = new VBox(15);
        card.setPadding(new Insets(30));
        card.setAlignment(Pos.CENTER);
        card.setStyle("-fx-background-color: rgba(255, 255, 255, 0.7); -fx-background-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0, 0, 0, 0.25), 10, 0, 0, 4);");

        // Username Field with Border
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setStyle("-fx-background-color: #ECF0F1; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #BDC3C7; -fx-border-width: 2px;");

        // Password Field with Border
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setStyle("-fx-background-color: #ECF0F1; -fx-padding: 10; -fx-border-radius: 5; -fx-background-radius: 5; -fx-border-color: #BDC3C7; -fx-border-width: 2px;");


        // Login Button
        Button loginButton = new Button("Login");
        loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 5;");
        loginButton.setOnMouseEntered(e -> loginButton.setStyle("-fx-background-color: #45A049; -fx-text-fill: white;"));
        loginButton.setOnMouseExited(e -> loginButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;"));

        // Error Message Label
        Label errorMessage = new Label();
        errorMessage.setStyle("-fx-text-fill: red; -fx-font-size: 12px;");

        // Arrange Card Elements
        card.getChildren().addAll(usernameField, passwordField, loginButton, errorMessage);

        // Add the login card (form) on top of the logo image in the center
        root.getChildren().add(card);

        // Scene Setup
        Scene loginScene = new Scene(root, 800, 600);
        primaryStage.setScene(loginScene);
        primaryStage.show();

        // Login Button Action
        loginButton.setOnAction(event -> {
            String username = usernameField.getText();
            String password = passwordField.getText();

            if (USERNAME.equals(username) && PASSWORD.equals(password)) {
                MainScreen mainScreen = new MainScreen();
                mainScreen.showMainScreen(primaryStage);  // Navigate to the main screen on success
            } else {
                errorMessage.setText("Invalid credentials. Please try again.");
            }
        });
    }
}