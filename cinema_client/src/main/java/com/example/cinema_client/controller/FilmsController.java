package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.FilmDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Film;
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
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.*;




public class FilmsController extends Menu implements Initializable {

    @FXML
    private TableView<Film> filmsTableView;


    @FXML
    private TableColumn<Film, String> nameColumn;

    @FXML
    private TableColumn<Film, String> dateColumn;

    @FXML
    private TableColumn<Film, String> studioColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button usersButton;

    @FXML
    private Button addSeanceButton;


    private List<Film> films;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!SessionStorage.isAdmin){
            addButton.setVisible(false);
            changeButton.setVisible(false);
            deleteButton.setVisible(false);
            usersButton.setVisible(false);
            addSeanceButton.setVisible(false);
        }
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        studioColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStudio()));
        setData();
    }


    private void setData(){
        FilmDto filmDto = new FilmDto();
        String request = filmDto.createRequest(new Film(), RequestType.GET_FILMS);
        ClientSocketTCP.send(request);
        String response = ClientSocketTCP.get();
        films = filmDto.getResponseEntityList(response);
        ObservableList<Film> filmObservableList;
        filmObservableList = FXCollections.observableArrayList();
        filmObservableList.addAll(films);
        filmsTableView.setItems(filmObservableList);
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event){
        Film selectedFilm = filmsTableView.getSelectionModel().getSelectedItem();
        if(selectedFilm == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вы точно хотите удалить фильм?");
        alert.setTitle("Подтверждение");
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        } else {
            FilmDto filmDto = new FilmDto();
            String request = filmDto.createRequest(selectedFilm, RequestType.DELETE_FILM);
            ClientSocketTCP.send(request);
            String  response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);
            if(responseType == ResponseType.SUCCESS)
                setData();
            else
                alert("Ошибка на сервере", "Не удалось удалить фильм!");
        }

    }

    @FXML
    private void onChangeButtonClick(ActionEvent event) throws IOException {
        Film selectedFilm = filmsTableView.getSelectionModel().getSelectedItem();
        if(selectedFilm == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("FilmAddChange.fxml"));
        Parent root = loader.load();
        FilmAddChangeController controller = loader.getController();
        controller.itIsChangeScene(selectedFilm);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onAddButtonClick(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("FilmAddChange.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    private void onAddSeanceButtonClick(ActionEvent event) throws IOException {
        Film selectedFilm = filmsTableView.getSelectionModel().getSelectedItem();
        if(selectedFilm == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SeanceAddChange.fxml"));
        Parent root = loader.load();
        SeanceAddChangeController controller = loader.getController();
        controller.itIsAddScene(selectedFilm);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }
}
