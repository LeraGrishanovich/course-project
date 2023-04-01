package com.example.cinema_client.Model.tcp;

import com.example.cinema_client.Enum.ResponseType;
import lombok.Data;

@Data
public class Response {
    private ResponseType responseType;
    private String responseMessage;

    public Response(ResponseType responseType, String responseMessage) {
        this.responseType = responseType;
        this.responseMessage = responseMessage;
    }
}
