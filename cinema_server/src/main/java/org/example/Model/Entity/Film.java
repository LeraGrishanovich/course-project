package org.example.Model.Entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "films")
public class Film {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_film")
    private Long id;

    private String name;

    private String date;

    private String studio;


    @OneToMany(mappedBy = "film", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Seance> seances;
}
