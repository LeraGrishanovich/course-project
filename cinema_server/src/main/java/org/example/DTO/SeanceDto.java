package org.example.DTO;


import com.google.gson.Gson;
import org.example.Enum.RequestType;
import org.example.Enum.ResponseType;
import org.example.Model.Entity.Film;
import org.example.Model.Entity.Seance;
import org.example.Model.tcp.Request;
import org.example.Model.tcp.Response;

import java.util.List;

public class SeanceDto {
    private Request request;


    public String createResponse(Seance seance, ResponseType responseType) {
        seance.setTickets(null);
        seance.getFilm().setSeances(null);
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(seance));
        result = new Gson().toJson(response);
        return result;
    }


    public Seance getRequestEntity(String result) {
        Seance seance;
        request = new Gson().fromJson(result, Request.class);
        seance = new Gson().fromJson(request.getRequestMessage(), Seance.class);
        return seance;
    }


    public RequestType getRequestType(String result) {
        request = new Gson().fromJson(result, Request.class);
        return request.getRequestType();
    }


    public String createResponse(List<Seance> seanceList, ResponseType responseType) {
        for(Seance seance : seanceList) {
            seance.setTickets(null);
            seance.getFilm().setSeances(null);
        }
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(seanceList));
        result = new Gson().toJson(response);
        return result;
    }
}
