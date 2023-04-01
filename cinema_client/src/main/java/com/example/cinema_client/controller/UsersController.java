package com.example.cinema_client.controller;


import com.example.cinema_client.DTO.UserDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.User;
import com.example.cinema_client.utility.ClientSocketTCP;
import com.example.cinema_client.utility.SessionStorage;
import com.google.gson.Gson;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UsersController extends Menu implements Initializable {
    @FXML
    private TableView<User> usersTableView;

    @FXML
    private Button addButton;

    @FXML
    private Button changeRoleButton;



    @FXML
    private Button changePasswordButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<User, String> loginColumn;

    @FXML
    private TableColumn<User, String> isAdminColumn;

    @FXML
    private TableColumn<User, String> hashPasswordColumn;

    private ObservableList<User> userObservableList;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getLogin()));
        hashPasswordColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPassword())));
        isAdminColumn.setCellValueFactory(data -> {
            if(data.getValue().isAdmin()) {
                return new SimpleStringProperty("Да");
            }
            else
                return new SimpleStringProperty("Нет");
        });
        setData();
    }


    @FXML
    private void setData(){
        UserDto userDto = new UserDto();
        String request = userDto.createRequest(new User(), RequestType.GET_USERS);
        ClientSocketTCP.send(request);
        String response = ClientSocketTCP.get();
        ArrayList<User> list = userDto.getResponseEntityList(response);
        userObservableList = FXCollections.observableArrayList();
        userObservableList.addAll(list);
        usersTableView.setItems(userObservableList);
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("UserAdd.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onChangePasswordButtonClick(ActionEvent event) throws IOException {
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if(selectedUser == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("UserChangePassword.fxml"));
        Parent root = loader.load();

        UserChangePasswordController controller = loader.getController();
        controller.setUserForUpdating(selectedUser);


        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event){
        User selectedUser = usersTableView.getSelectionModel().getSelectedItem();
        if(selectedUser == null)
            return;
        UserDto userDto = new UserDto();
        String request = userDto.createRequest(selectedUser, RequestType.DELETE_USER);
        ClientSocketTCP.send(request);
        String  response = ClientSocketTCP.get();
        ResponseType responseType = new Gson().fromJson(response, ResponseType.class);
        if(responseType == ResponseType.SUCCESS)
            setData();
        else
            alert("Ошибка на сервербе", "Не удалось удалить пользователя!");
    }
}
