package com.example.cinema_client.controller;


import com.example.cinema_client.DTO.FilmDto;
import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Model.entity.Film;
import com.example.cinema_client.Model.entity.Seance;
import com.example.cinema_client.utility.ClientSocketTCP;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import org.apache.commons.math3.util.Precision;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FilmStatisticController extends Menu implements Initializable {

    @FXML
    private BarChart<Number, String> barChart;

    private double totalTicketsForFilm = 0;


    List<Film> films = new ArrayList<>();

    public void initialize(URL url, ResourceBundle resourceBundle){
        XYChart.Series dataSeries = new XYChart.Series();
        dataSeries.setName("Среднее число купленных билетов на сеанс");

        FilmDto filmDto = new FilmDto();
        String request = filmDto.createRequest(new Film(), RequestType.GET_FILMS);
        ClientSocketTCP.send(request);
        String response = ClientSocketTCP.get();
        films = filmDto.getResponseEntityList(response);

        films.forEach(film -> {
            totalTicketsForFilm = 0;
            for(Seance seance : film.getSeances()){
                totalTicketsForFilm += seance.getTickets().size();
            }
            if(totalTicketsForFilm != 0) {
                Double averageTicketsPerSeance = Precision.round(totalTicketsForFilm / film.getSeances().size(), 2);
                dataSeries.getData().add(new XYChart.Data(film.getName() + "\nБилеты: " + averageTicketsPerSeance, averageTicketsPerSeance));
            }

        });

        barChart.getData().add(dataSeries);
        barChart.setTitle("Статистика продажи билетов");
    }
}
