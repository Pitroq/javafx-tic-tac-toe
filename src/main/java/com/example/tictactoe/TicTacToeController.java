package com.example.tictactoe;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TicTacToeController implements Initializable {
    @FXML
    private Label currentPlayerLabel;
    @FXML
    private GridPane board;


    private static final int BOARD_COLS = 3;
    private static final int BOARD_ROWS = 3;
    private String player = "x";
    private String winner = "";
    private URL FXMlLocation;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        FXMlLocation = location;
        updateCurrentPlayerLabel();
    }

    private void changePlayer() {
        if (player == "x") player = "o";
        else if (player == "o") player = "x";

        updateCurrentPlayerLabel();
    }

    private void updateCurrentPlayerLabel() {
        if (player.equals("x")) {
            currentPlayerLabel.getStyleClass().remove("o-color");
            currentPlayerLabel.getStyleClass().add("x-color");
        }
        else if (player.equals("o")) {
            currentPlayerLabel.getStyleClass().remove("x-color");
            currentPlayerLabel.getStyleClass().add("o-color");
        }
        currentPlayerLabel.setText(player);
    }

    public void setOwner(ActionEvent event) {
        Button button = ((Button) event.getSource());
        if (!button.getText().isEmpty() | !winner.isEmpty()) {
            return;
        }

        button.setText(player);
        button.getStyleClass().add(player + "-color");
        checkBoard();
        changePlayer();
    }
    private String[][] boardToStringArray() {
        String[][] board = new String[BOARD_COLS][BOARD_ROWS];
        ObservableList<Node> childrens = this.board.getChildren();

        int col = 0, row = 0;
        for (Node node : childrens) {
            board[col][row] = ((Button) node).getText();

            row += 1;
            if (row == BOARD_ROWS) {
                row = 0;
                col += 1;
            }
        }

        return board;
    }

    private boolean isDraw() {
        ObservableList<Node> childrens = board.getChildren();

        for (Node node : childrens) {
            if (((Button) node).getText().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String whichPlayerWon(String fieldSum) {
        if (fieldSum.equals("xxx")) return "x";
        else if (fieldSum.equals("ooo")) return "o";
        else return "";
    }

    private void checkHorizontalWinner(String[][] stringBoard) {
        for (int col = 0; col < BOARD_COLS; col++) {
            StringBuilder rowSum = new StringBuilder();
            for (int row = 0; row < BOARD_ROWS; row++) {
                rowSum.append(stringBoard[col][row]);
            }
            String winner = whichPlayerWon(rowSum.toString());
            if (!winner.isEmpty()) {
                this.winner = winner;
                return;
            }
        }
    }

    private void checkVerticalWinner(String[][] stringBoard) {
        for (int row = 0; row < BOARD_COLS; row++) {
            StringBuilder colSum = new StringBuilder();
            for (int col = 0; col < BOARD_ROWS; col++) {
                colSum.append(stringBoard[col][row]);
            }
            String winner = whichPlayerWon(colSum.toString());
            if (!winner.isEmpty()) {
                this.winner = winner;
                return;
            }
        }
    }

    private void checkCrossWinner(String[][] stringBoard) {
        StringBuilder crossSum = new StringBuilder();
        for (int row = 0; row < BOARD_COLS; row++) {
            crossSum.append(stringBoard[row][row]);
        }

        String winner = whichPlayerWon(crossSum.toString());
        if (!winner.isEmpty()) {
            this.winner = winner;
            return;
        }

        crossSum = new StringBuilder();
        for (int row = 0, col = BOARD_ROWS - 1; row < BOARD_COLS; row++, col--) {
            crossSum.append(stringBoard[col][row]);
        }
        winner = whichPlayerWon(crossSum.toString());
        if (!winner.isEmpty()) {
            this.winner = winner;
        }

    }

    public void resetBoard() {
        FXMLLoader fxmlLoader = new FXMLLoader(FXMlLocation);
        Stage stage = (Stage) board.getScene().getWindow();
        Scene scene = null;
        try {
            scene = new Scene(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        String style = getClass().getResource("styles/style.css").toExternalForm();
        scene.getStylesheets().add(style);
        stage.setScene(scene);
        stage.show();
    }

    private void createEndOfGameAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("End of game");
        alert.getDialogPane().setStyle("-fx-font-size: 15px;");
        alert.setHeaderText(message);
        alert.setContentText("Do you want to play again?");

        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == ButtonType.OK) {
                resetBoard();
            }
            else if (buttonType == ButtonType.CANCEL) {
                Stage stage = (Stage) board.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void checkBoard() {
        String[][] stringBoard = boardToStringArray();

        checkHorizontalWinner(stringBoard);
        checkVerticalWinner(stringBoard);
        checkCrossWinner(stringBoard);

        String alertMessage;
        if (winner.isEmpty()) {
            if (isDraw()) {
                alertMessage = "Draw!";
            }
            else {
                return;
            }
        }
        else {
            alertMessage = "Winner: " + winner.toUpperCase() + "!";
        }

        createEndOfGameAlert(alertMessage);
    }
}