package com.example.cinema_client.Model.entity;


import lombok.Data;

import java.util.List;

@Data
public class Film {

    private Long id;

    private String name;

    private String date;

    private String studio;

    private List<Seance> seances;
}
