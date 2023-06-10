package com.example.tictactoe;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("layouts/tic-tac-toe.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        String style = getClass().getResource("styles/style.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setTitle("Tic Tac Toe!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}