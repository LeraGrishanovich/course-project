package com.example.cinema_client.controller;

import com.example.cinema_client.DTO.FilmDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Film;
import com.example.cinema_client.utility.ClientSocketTCP;
import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;

public class FilmAddChangeController extends Menu{
    @FXML
    private DatePicker datePicker;
    @FXML
    private Button addOrUpdateButton;

    @FXML
    private TextField nameField;

    @FXML
    private TextField studioField;
    private boolean isChange = false;

    private Film selectedFilm;

    public void itIsChangeScene(Film film){

        isChange = true;
        nameField.setText(film.getName());
        studioField.setText(String.valueOf(film.getStudio()));
        datePicker.getEditor().setText(film.getDate());
        addOrUpdateButton.setText("Обновить");
        selectedFilm = film;
    }

    @FXML
    private void onAddOrUpdateButtonClick(ActionEvent event) throws IOException {
        if(nameField.getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }

        if(datePicker.getEditor().getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }

        if(studioField.getText().equals("")){
            alert("Поля должны быть заполнены!", "Ошибка ввода");
            return;
        }



        if (isChange){
            FilmDto filmDto = new FilmDto();
            selectedFilm.setName(nameField.getText());
            selectedFilm.setStudio(studioField.getText());
            selectedFilm.setDate(datePicker.getEditor().getText());
            String request = filmDto.createRequest(selectedFilm, RequestType.UPDATE_FILM);
            ClientSocketTCP.send(request);
            String response;
            response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);



            if(ResponseType.SUCCESS == responseType){
                alert("Фильм успешно обновлен!", "Обновление");
                onFilmsButtonClick(event);
            }
            else if(ResponseType.FAIL == responseType){
                alert("Ошибка!", "Не удалось обновить фильм");
            }
        }
        else {
            Film filmForAdding = new Film();
            filmForAdding.setName(nameField.getText());
            filmForAdding.setStudio(studioField.getText());
            filmForAdding.setDate(datePicker.getEditor().getText());
            FilmDto filmDto = new FilmDto();

            String request = filmDto.createRequest(filmForAdding, RequestType.ADD_FILM);
            ClientSocketTCP.send(request);


            String response;
            response = ClientSocketTCP.get();
            ResponseType responseType = new Gson().fromJson(response, ResponseType.class);



            if(ResponseType.SUCCESS == responseType){
                alert("Фильм успешно добавлен!", "Добавление");
                onFilmsButtonClick(event);
            }
            else if(ResponseType.FAIL == responseType){
                alert("Ошибка!", "Не удалось добавить фильм");
            }
        }

    }

}
