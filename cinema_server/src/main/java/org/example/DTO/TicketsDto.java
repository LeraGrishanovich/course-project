package org.example.DTO;


import com.google.gson.Gson;
import org.example.Enum.RequestType;
import org.example.Enum.ResponseType;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.Ticket;
import org.example.Model.tcp.Request;
import org.example.Model.tcp.Response;

import java.util.List;

public class TicketsDto {
    private Request request;


    public String createResponse(Ticket ticket, ResponseType responseType) {
        ticket.getSeance().setTickets(null);
        ticket.getSeance().getFilm().setSeances(null);
        ticket.getUser().setTickets(null);
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(ticket));
        result = new Gson().toJson(response);
        return result;
    }


    public Ticket getRequestEntity(String result) {
        Ticket ticket;
        request = new Gson().fromJson(result, Request.class);
        ticket = new Gson().fromJson(request.getRequestMessage(), Ticket.class);
        return ticket;
    }


    public RequestType getRequestType(String result) {
        request = new Gson().fromJson(result, Request.class);
        return request.getRequestType();
    }


    public String createResponse(List<Ticket> ticketList, ResponseType responseType) {
        for(Ticket ticket : ticketList) {
            ticket.getSeance().setTickets(null);
            ticket.getSeance().getFilm().setSeances(null);
            ticket.getUser().setTickets(null);
        }
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(ticketList));
        result = new Gson().toJson(response);
        return result;
    }
}
