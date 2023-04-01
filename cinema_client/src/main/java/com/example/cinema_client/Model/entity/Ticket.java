package com.example.cinema_client.Model.entity;

import lombok.Data;


@Data
public class Ticket {

    private Long id;

    private Seance seance;

    private User user;
}
