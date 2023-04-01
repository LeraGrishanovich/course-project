package com.example.cinema_client.DTO;


import com.example.cinema_client.Enum.RequestType;
import com.example.cinema_client.Enum.ResponseType;
import com.example.cinema_client.Model.entity.User;
import com.example.cinema_client.Model.tcp.Request;
import com.example.cinema_client.Model.tcp.Response;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class UserDto{
    private Response response;
    private Request request;


    public String createRequest(User entity, RequestType requestType) {
        String result;
        request = new Request(requestType, new Gson().toJson(entity));
        result = new Gson().toJson(request);
        return result;
    }



    public ArrayList<User> getResponseEntityList(String result) {
        ArrayList<User> users;
        Response response = new Gson().fromJson(result, Response.class);
        users = new Gson().fromJson(response.getResponseMessage(), new TypeToken<List<User>>(){}.getType());
        return users;
    }


    public User getResponseEntity(String result) {
        User user;
        response = new Gson().fromJson(result, Response.class);
        user = new Gson().fromJson(response.getResponseMessage(), User.class);
        return user;
    }


    public ResponseType getResponseType(String result) {
        response = new Gson().fromJson(result,Response.class);
        return response.getResponseType();
    }
}
