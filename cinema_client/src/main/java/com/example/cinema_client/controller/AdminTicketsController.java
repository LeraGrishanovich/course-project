package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.SeanceDto;
import com.example.cinema_client.DTO.TicketDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
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

public class AdminTicketsController extends Menu implements Initializable {






    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Ticket> ticketsTableView;


    @FXML
    private TableColumn<Ticket, String> filmColumn;

    @FXML
    private TableColumn<Ticket, String> dateColumn;

    @FXML
    private TableColumn<Ticket, String> timeColumn;

    @FXML
    private TableColumn<Ticket, String> priceColumn;

    @FXML
    private TableColumn<Ticket, String> userColumn;

    @FXML
    private TableColumn<Ticket, String> idColumn;

    private List<Ticket> tickets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        userColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUser().getLogin()));
        filmColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getFilm().getName()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getDate()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getTime()));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getPrice() +" BYN"));
        setData();
    }


    private void setData(){
        TicketDto ticketDto = new TicketDto();
        String request = ticketDto.createRequest(new Ticket(), RequestType.GET_TICKETS);
        ClientSocketTCP.send(request);
        String response = ClientSocketTCP.get();
        tickets = ticketDto.getResponseEntityList(response);
        ObservableList<Ticket> ticketObservableList;
        ticketObservableList = FXCollections.observableArrayList();
        ticketObservableList.addAll(tickets);
        ticketsTableView.setItems(ticketObservableList);
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event){
        Ticket selectedTicket = ticketsTableView.getSelectionModel().getSelectedItem();
        if(selectedTicket == null)
            return;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Вы точно хотите удалить билет?");
        alert.setTitle("Подтверждение");
        Optional<ButtonType> result = alert.showAndWait();
        if(!result.isPresent() || result.get() != ButtonType.OK) {
            return;
        } else {
            TicketDto ticketDto = new TicketDto();
            String request = ticketDto.createRequest(selectedTicket, RequestType.DELETE_TICKET);
            ClientSocketTCP.send(request);
            String  response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);
            if(responseType == ResponseType.SUCCESS)
                setData();
            else
                alert("Ошибка на сервере", "Не удалось удалить билет!");
        }

    }
}
