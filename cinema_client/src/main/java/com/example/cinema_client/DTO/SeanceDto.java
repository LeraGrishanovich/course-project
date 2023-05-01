package com.example.cinema_client.DTO;


import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.Seance;
import com.example.cinema_client.Model.tcp.Request;
import com.example.cinema_client.Model.tcp.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SeanceDto {
    private Response response;
    private Request request;


    public String createRequest(Seance entity, RequestType requestType) {
        String result;
        request = new Request(requestType, new Gson().toJson(entity));
        result = new Gson().toJson(request);
        return result;
    }



    public ArrayList<Seance> getResponseEntityList(String result) {
        ArrayList<Seance> seances;
        Response response = new Gson().fromJson(result, Response.class);
        seances = new Gson().fromJson(response.getResponseMessage(), new TypeToken<List<Seance>>(){}.getType());
        return seances;
    }


    public Seance getResponseEntity(String result) {
        Seance seance;
        response = new Gson().fromJson(result, Response.class);
        seance = new Gson().fromJson(response.getResponseMessage(), Seance.class);
        return seance;
    }


    public ResponseType getResponseType(String result) {
        response = new Gson().fromJson(result,Response.class);
        return response.getResponseType();
    }
}
