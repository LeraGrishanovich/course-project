package org.example.Model.Entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "seances")
public class Seance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_seance")
    private Long id;

    private String time;

    private double price;

    private String date;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "film_id")
    private Film film;



    @OneToMany(mappedBy = "seance", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Ticket> tickets;

}
