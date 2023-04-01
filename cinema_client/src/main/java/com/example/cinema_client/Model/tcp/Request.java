package com.example.cinema_client.Model.tcp;

import com.example.cinema_client.Enum.RequestType;
import lombok.Data;

@Data
public class Request {

    private RequestType requestType;

    private String requestMessage;

    public Request(RequestType requestType, String requestMessage) {
        this.requestType = requestType;
        this.requestMessage = requestMessage;
    }
}
