package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "halls",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_hall_cinema_name", columnNames = {"cinema_id", "name"})
        }
)
public class Hall {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @ManyToOne(optional = false)
    @JoinColumn(name = "cinema_id", nullable = false)
    private Cinema cinema;

    protected Hall() {
    }

    public Hall(String name, Cinema cinema) {
        this.name = name;
        this.cinema = cinema;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Cinema getCinema() {
        return cinema;
    }

    public void setCinema(Cinema cinema) {
        this.cinema = cinema;
    }
}
