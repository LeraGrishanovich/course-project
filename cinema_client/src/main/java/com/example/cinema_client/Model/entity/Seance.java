package com.example.cinema_client.Model.entity;


import lombok.Data;

import java.util.List;


@Data
public class Seance {

    private Long id;

    private String date;

    private String time;

    private double price;

    private Film film;

    private List<Ticket> tickets;
}
