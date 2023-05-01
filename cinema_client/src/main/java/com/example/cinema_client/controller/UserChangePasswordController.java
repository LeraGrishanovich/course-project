package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.UserDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.User;
import com.example.cinema_client.utility.ClientSocketTCP;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class UserChangePasswordController {


    @FXML
    private PasswordField passwordHideField;
    @FXML
    private TextField passwordShowField;

    private TextField passwordField;

    @FXML
    private ImageView passwordImageView;

    @FXML
    private Label loginLabel;

    private User userForUpdating;

    public  void setUserForUpdating(User user){
        passwordField = passwordHideField;
        userForUpdating = user;
        loginLabel.setText(userForUpdating.getLogin());
    }




    @FXML
    protected void onPasswordImageViewClick(){
        if(passwordHideField.isVisible()) {
            passwordImageView.setImage(new Image("showed_icon.png"));
            passwordHideField.setVisible(false);
            passwordShowField.setVisible(true);
            passwordField = passwordShowField;
            passwordShowField.setText(passwordHideField.getText());
        }
        else {
            passwordImageView.setImage(new Image("hidden_icon.png"));
            passwordShowField.setVisible(false);
            passwordHideField.setVisible(true);
            passwordField = passwordHideField;
            passwordHideField.setText(passwordShowField.getText());
        }
    }

    @FXML
    private void onUpdateButtonClick(ActionEvent event) throws IOException {
        boolean error = false;

        int password = User.RSHash(passwordField.getText());
        UserDto userDto = new UserDto();



        if(passwordField.getText().length() < 5){
            error = true;
        }

        if(error){
            Alert(" Пароль должен быть длинной не менее пяти символов!", "Ошибка ввода");
            return;
        }

        userForUpdating.setPassword(password);


        String request = userDto.createRequest(userForUpdating, RequestType.UPDATE_USER);
        ClientSocketTCP.send(request);
        String response;
        response = ClientSocketTCP.get();
        ResponseType responseType = new Gson().fromJson(response, ResponseType.class);

        if(ResponseType.SUCCESS == responseType){
            Alert("Пароль успешно изменен!", "Изменение пароля");
            loadUsersScene(event);
        }
        else {
            Alert("Неопознанный ответ сервера!", "Ошибка");
        }


    }

    public void Alert(String str1, String str2) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(str1);
        alert.setTitle(str2);
        alert.showAndWait();
    }

    @FXML
    private void loadUsersScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Users.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
