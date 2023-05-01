package com.example.cinema_client.controller;

import com.example.cinema_client.utility.SessionStorage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class Menu {



    private void changeScene(ActionEvent event, String source) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(Menu.class.getResource(source)));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

     @FXML
     void onTicketsButtonClick(ActionEvent event) throws IOException {
        if(SessionStorage.isAdmin)
            changeScene(event,"AdminTickets.fxml");
        else
            changeScene(event,"UserTickets.fxml");
    }

    @FXML
    void onFilmsButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"Films.fxml");
    }

    @FXML
    void onSeanceButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"Seances.fxml");

    }

    @FXML
    void onUsersButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"Users.fxml");
    }

    @FXML
    void onExitButtonClick(ActionEvent event) throws IOException {
        changeScene(event,"authorization.fxml");
    }

    protected void alert(String text, String header) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(text);
        alert.setTitle(header);
        alert.showAndWait();
    }

    protected void alert(String text, String header, String additionalText) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(text);
        alert.setContentText(additionalText);
        alert.setTitle(header);
        alert.showAndWait();
    }
}
