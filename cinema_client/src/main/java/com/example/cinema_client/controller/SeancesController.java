package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.FilmDto;
import com.example.cinema_client.DTO.SeanceDto;
import com.example.cinema_client.DTO.TicketDto;
import com.example.cinema_client.DTO.UserDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Film;
import com.example.cinema_client.Model.entity.Seance;
import com.example.cinema_client.Model.entity.Ticket;
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
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;


public class SeancesController extends Menu implements Initializable {

    @FXML
    private Button usersButton;

    @FXML
    private Button buyButton;

    @FXML
    private Button changeButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Seance> seancesTableView;


    @FXML
    private TableColumn<Seance, String> filmColumn;

    @FXML
    private TableColumn<Seance, String> dateColumn;

    @FXML
    private TableColumn<Seance, String> timeColumn;

    @FXML
    private TableColumn<Seance, String> priceColumn;

    private List<Seance> seances;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if(!SessionStorage.isAdmin){
            usersButton.setVisible(false);
            changeButton.setVisible(false);
            deleteButton.setVisible(false);
        }
        else
            buyButton.setVisible(false);
        filmColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getFilm().getName()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTime()));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPrice())+" BYN"));
        setData();
    }


    private void setData(){
        SeanceDto seanceDto = new SeanceDto();
        String request = seanceDto.createRequest(new Seance(), RequestType.GET_SEANCES);
        ClientSocketTCP.send(request);
        String response = ClientSocketTCP.get();
        seances = seanceDto.getResponseEntityList(response);
        ObservableList<Seance> seanceObservableList;
        seanceObservableList = FXCollections.observableArrayList();
        seanceObservableList.addAll(seances);
        seancesTableView.setItems(seanceObservableList);
    }


    @FXML
    private void onUpdateSeanceButtonClick(ActionEvent event) throws IOException {
        Seance selectedSeance = seancesTableView.getSelectionModel().getSelectedItem();
        if(selectedSeance == null)
            return;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("SeanceAddChange.fxml"));
        Parent root = loader.load();
        SeanceAddChangeController controller = loader.getController();
        controller.itIsChangeScene(selectedSeance);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onBuyButtonClick(ActionEvent event) throws IOException {
        Seance selectedSeance = seancesTableView.getSelectionModel().getSelectedItem();
        if(selectedSeance == null)
            return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вы точно хотите купить билет?");
        alert.setTitle("Подтверждение");
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        } else {
            Ticket ticket = new Ticket();
            ticket.setSeance(selectedSeance);
            ticket.setUser(SessionStorage.user);
            TicketDto ticketDto = new TicketDto();
            String request = ticketDto.createRequest(ticket, RequestType.ADD_TICKET);
            ClientSocketTCP.send(request);
            String  response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);
            if(responseType == ResponseType.SUCCESS)
                alert("Вы успешно приобрели билет!\nПросмотреть купленные билеты можно во вкладке билеты", "Покупка билета");
            else
                alert("Ошибка на сервере", "Не удалось купить билет!");
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event){
        Seance selectedSeance = seancesTableView.getSelectionModel().getSelectedItem();
        if(selectedSeance == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вы точно хотите удалить сеанс?");
        alert.setTitle("Подтверждение");
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        } else {
            SeanceDto seanceDto = new SeanceDto();
            String request = seanceDto.createRequest(selectedSeance, RequestType.DELETE_SEANCE);
            ClientSocketTCP.send(request);
            String  response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);
            if(responseType == ResponseType.SUCCESS)
                setData();
            else
                alert("Ошибка на сервере", "Не удалось удалить сеанс!");
        }

    }
}
