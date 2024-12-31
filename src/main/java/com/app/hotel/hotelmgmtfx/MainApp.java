package com.app.hotel.hotelmgmtfx;

import com.app.hotel.hotelmgmtfx.screens.LoginScreen;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        LoginScreen loginScreen = new LoginScreen();
        loginScreen.showLoginScreen(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}