package com.example.cinema_client.Model.entity;


import lombok.Data;



@Data
public class Seance {

    private Long id;

    private String date;

    private String time;

    private double price;

    private Film film;
}
