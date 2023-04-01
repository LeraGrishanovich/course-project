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
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {


    @FXML
    private TextField loginField;

    @FXML
    private RadioButton radioAdmin;

    @FXML
    private Button registrationButton;

    @FXML
    private PasswordField passwordHideField;
    @FXML
    private TextField passwordShowField;

    private TextField passwordField;

    @FXML
    private ImageView passwordImageView;




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
    private void onRegistrationButtonClick(ActionEvent event) throws IOException {
        boolean error = false;
        String login = loginField.getText();
        int password = User.RSHash(passwordField.getText());
        UserDto userDto = new UserDto();
        if(loginField.getText().length() < 5){
            error = true;
        }
        if(passwordField.getText().length() < 5){
            error = true;
        }

        if(error){
            Alert("Логин и Пароль должны быть длинной не менее пяти символов,\nа остальные поля должны быть заполнены!", "Ошибка ввода");
            return;
        }
        User user = new User();
        user.setLogin(login);
        user.setPassword(password);
        if (radioAdmin.isSelected()){
            user.setAdmin(true);
        }
        else {
            user.setAdmin(false);
        }



        String request = userDto.createRequest(user, RequestType.REGISTRATION);
        ClientSocketTCP.send(request);
        String response;
        response = ClientSocketTCP.get();
        ResponseType responseType = new Gson().fromJson(response, ResponseType.class);

        if(ResponseType.SUCCESS == responseType){
            Alert("Вы успешно зарегистрировались!", "Регистрация");
            onRegistrationSuccess(event);
        }
        else if(ResponseType.FAIL == responseType){
                Alert("Пользователь с таким логином уже существует!", "Не удалось зарегистрироваться");
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
    private void loadAuthorizationScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("authorization.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void loadUsersScene(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Users.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onRegistrationSuccess(ActionEvent event) throws IOException {

        if(registrationButton.getText().equals("Добавить")){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Users.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
        }
        else{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("authorization.fxml"));
            Parent root = loader.load();

            AuthorizationController controller = loader.getController();
            controller.setLogData(loginField.getText(), passwordField.getText());


            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);

            stage.setScene(scene);
        }


    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        passwordField = passwordHideField;
    }


}
