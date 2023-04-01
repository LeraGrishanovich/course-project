package com.example.cinema_client.controller;


import com.example.cinema_client.DTO.FilmDto;
import com.example.cinema_client.DTO.SeanceDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Film;
import com.example.cinema_client.Model.entity.Seance;
import com.example.cinema_client.utility.ClientSocketTCP;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class SeanceAddChangeController extends Menu{
    private boolean isChange = false;
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addOrUpdateButton;

    @FXML
    private TextField priceField;

    @FXML
    private TextField timeField;

    @FXML
    private Label filmLabel;


    private Film selectedFilm;

    private Seance selectedSeance;

    public void itIsChangeScene(Seance seance){
        isChange = true;
        filmLabel.setText("Фильм: " + seance.getFilm().getName());
        priceField.setText(String.valueOf(seance.getPrice()));
        timeField.setText(seance.getTime());
        datePicker.getEditor().setText(seance.getDate());
        addOrUpdateButton.setText("Обновить");
        selectedSeance = seance;
    }

    public void itIsAddScene(Film film){
        isChange = false;
        filmLabel.setText("Фильм: " + film.getName());
        selectedFilm = film;
    }

    @FXML
    private void onBackButtonClick(ActionEvent event) throws IOException {
        String str;
        if(isChange)
            str = "Seances.fxml";
        else
            str = "Films.fxml";

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(str));
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
    }

    @FXML
    private void onAddOrUpdateButtonClick(ActionEvent event) throws IOException {
        if(priceField.getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }

        if(datePicker.getEditor().getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }

        if(timeField.getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }

        double price;

        try {
            price = Double.parseDouble(priceField.getText());
        }
        catch (RuntimeException e){
            alert("Неверная цена!", "Ошибка ввода");
            return;
        }

        String time;
        time = timeField.getText();
        if(!time.matches("^(([0,1][0-9])|(2[0-3])):[0-5][0-9]$")){
            alert("Правильный формат:\nhh:mm", "Неверный формат времени!");
            return;
        }




        if (isChange){
            SeanceDto seanceDto = new SeanceDto();
            selectedSeance.setPrice(price);
            selectedSeance.setTime(time);
            selectedSeance.setDate(datePicker.getEditor().getText());

            String request = seanceDto.createRequest(selectedSeance, RequestType.UPDATE_SEANCE);
            ClientSocketTCP.send(request);
            String response;
            response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);



            if(ResponseType.SUCCESS == responseType){
                alert("Сеанс успешно обновлен!", "Обновление");
                onSeanceButtonClick(event);
            }
            else if(ResponseType.FAIL == responseType){
                alert("Ошибка!", "Не удалось обновить сеанс");
            }
        }
        else {
            Seance seanceForAdding = new Seance();
            seanceForAdding.setPrice(price);
            seanceForAdding.setTime(time);
            seanceForAdding.setDate(datePicker.getEditor().getText());
            seanceForAdding.setFilm(selectedFilm);

            SeanceDto seanceDto = new SeanceDto();

            String request = seanceDto.createRequest(seanceForAdding, RequestType.ADD_SEANCE);
            ClientSocketTCP.send(request);


            String response;
            response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);



            if(ResponseType.SUCCESS == responseType){
                alert("Сеанс успешно добавлен!", "Добавление");
                onFilmsButtonClick(event);
            }
            else if(ResponseType.FAIL == responseType){
                alert("Ошибка!", "Не удалось добавить сеанс");
            }
        }

    }
}
