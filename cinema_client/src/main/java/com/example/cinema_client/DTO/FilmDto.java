package com.example.cinema_client.DTO;


import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Film;
import com.example.cinema_client.Model.tcp.Request;
import com.example.cinema_client.Model.tcp.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;


import java.util.ArrayList;
import java.util.List;

public class FilmDto {
    private Response response;
    private Request request;


    public String createRequest(Film entity, RequestType requestType) {
        String result;
        request = new Request(requestType, new Gson().toJson(entity));
        result = new Gson().toJson(request);
        return result;
    }



    public ArrayList<Film> getResponseEntityList(String result) {
        ArrayList<Film> subjects;
        Response response = new Gson().fromJson(result, Response.class);
        subjects = new Gson().fromJson(response.getResponseMessage(), new TypeToken<List<Film>>(){}.getType());
        return subjects;
    }


    public Film getResponseEntity(String result) {
        Film film;
        response = new Gson().fromJson(result, Response.class);
        film = new Gson().fromJson(response.getResponseMessage(), Film.class);
        return film;
    }


    public ResponseType getResponseType(String result) {
        response = new Gson().fromJson(result,Response.class);
        return response.getResponseType();
    }
}
