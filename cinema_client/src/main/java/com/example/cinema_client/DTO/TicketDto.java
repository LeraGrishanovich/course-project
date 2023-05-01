package com.example.cinema_client.DTO;


import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Seance;
import com.example.cinema_client.Model.entity.Ticket;
import com.example.cinema_client.Model.tcp.Request;
import com.example.cinema_client.Model.tcp.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TicketDto {
    private Response response;
    private Request request;


    public String createRequest(Ticket entity, RequestType requestType) {
        String result;
        request = new Request(requestType, new Gson().toJson(entity));
        result = new Gson().toJson(request);
        return result;
    }



    public ArrayList<Ticket> getResponseEntityList(String result) {
        ArrayList<Ticket> tickets;
        Response response = new Gson().fromJson(result, Response.class);
        tickets = new Gson().fromJson(response.getResponseMessage(), new TypeToken<List<Ticket>>(){}.getType());
        return tickets;
    }


    public Ticket getResponseEntity(String result) {
        Ticket ticket;
        response = new Gson().fromJson(result, Response.class);
        ticket = new Gson().fromJson(response.getResponseMessage(), Ticket.class);
        return ticket;
    }


    public ResponseType getResponseType(String result) {
        response = new Gson().fromJson(result,Response.class);
        return response.getResponseType();
    }
}
