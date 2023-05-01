package org.example.DTO;


import com.google.gson.Gson;
import org.example.Enum.RequestType;
import org.example.Enum.ResponseType;
import org.example.Model.Entity.Film;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.Ticket;
import org.example.Model.tcp.Request;
import org.example.Model.tcp.Response;

import java.util.List;

public class FilmDto {
    private Request request;


    public String createResponse(Film film, ResponseType responseType) {
        film.setSeances(null);
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(film));
        result = new Gson().toJson(response);
        return result;
    }


    public Film getRequestEntity(String result) {
        Film film;
        request = new Gson().fromJson(result, Request.class);
        film = new Gson().fromJson(request.getRequestMessage(), Film.class);
        return film;
    }


    public RequestType getRequestType(String result) {
        request = new Gson().fromJson(result, Request.class);
        return request.getRequestType();
    }


    public String createResponse(List<Film> filmList, ResponseType responseType) {
//        for(Film film : filmList) {
//            film.setSeances(null);
//        }

        for(Film film : filmList) {
            for(Seance seance : film.getSeances()){
                seance.setFilm(null);
                for(Ticket ticket : seance.getTickets()){
                    ticket.setSeance(null);
                    ticket.setUser(null);
                }
            }
        }
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(filmList));
        result = new Gson().toJson(response);
        return result;
    }
}
