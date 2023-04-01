package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.SeanceDto;
import com.example.cinema_client.DTO.TicketDto;
import com.example.cinema_client.DTO.UserDto;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserTicketsController extends Menu implements Initializable {

    @FXML
    private Button printButton;

    @FXML
    private Label loginLabel;

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
    private TableColumn<Ticket, String> idColumn;

    private List<Ticket> tickets;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loginLabel.setText("Ваш логин: " + SessionStorage.user.getLogin());
        idColumn.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getId())));
        filmColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getFilm().getName()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getDate()));
        timeColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getTime()));
        priceColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSeance().getPrice() +" BYN"));
        setData();
    }


    private void setData(){
        UserDto userDto = new UserDto();
        TicketDto ticketDto = new TicketDto();
        String request = userDto.createRequest(SessionStorage.user, RequestType.GET_TICKETS_FOR_ONE_USER);
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

    @FXML
    private void onPrintButtonClick(ActionEvent event){
        try {
            // к которой будем прикручивать наполнение (колонтитулы, текст)
            Ticket selectedTicket = ticketsTableView.getSelectionModel().getSelectedItem();
            if(selectedTicket == null)
                return;
            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH-mm-ss");
            File file = new File("Tickets/Билет №" + selectedTicket.getId() + " от " + formatter.format(date) + ".txt");
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath());
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("---------- № " + selectedTicket.getId() + " ----------\n")
                    .append(" Фильм: " + selectedTicket.getSeance().getFilm().getName() + "\n")
                    .append(" Дата: " + selectedTicket.getSeance().getDate() + "\n")
                    .append(" Время: " + selectedTicket.getSeance().getTime() + "\n")
                    .append(" Стоимость: " + selectedTicket.getSeance().getPrice() + "\n");
            fos.write(stringBuffer.toString().getBytes());
            fos.close();
            alert("Билет распечатан\n" +
                    "Вы можете найти его в папке Tickets", "Печать");
        } catch (Exception e) {
            alert("Не удалось распечатать билет", "Печать");
            e.printStackTrace();
        }

    }
}