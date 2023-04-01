package com.example.cinema_client.controller;

import com.example.cinema_client.utility.SessionStorage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class TicketsController extends Menu implements Initializable {
    @FXML
    private Button usersButton;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!SessionStorage.isAdmin){
            usersButton.setVisible(false);
        }
    }
}
