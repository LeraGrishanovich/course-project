package org.example.DTO;


import com.google.gson.Gson;
import org.example.Enum.RequestType;
import org.example.Enum.ResponseType;
import org.example.Model.Entity.Seance;
import org.example.Model.Entity.User;
import org.example.Model.tcp.Request;
import org.example.Model.tcp.Response;

import java.util.List;

public class UserDto {
    private Request request;


    public String createResponse(User user, ResponseType responseType) {
        user.setTickets(null);
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(user));
        result = new Gson().toJson(response);
        return result;
    }


    public User getRequestEntity(String result) {
        User user;
        request = new Gson().fromJson(result, Request.class);
        user = new Gson().fromJson(request.getRequestMessage(), User.class);
        return user;
    }


    public RequestType getRequestType(String result) {
        request = new Gson().fromJson(result, Request.class);
        return request.getRequestType();
    }


    public String createResponse(List<User> userList, ResponseType responseType) {
        for(User user : userList)
            user.setTickets(null);
        String result = "";
        Response response = new Response(responseType, new Gson().toJson(userList));
        result = new Gson().toJson(response);
        return result;
    }
}
