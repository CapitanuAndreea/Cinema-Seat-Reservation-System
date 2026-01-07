package com.example.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Entity
@Table(
        name = "seats",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_seat_hall_row_number", columnNames = {"hall_id", "row_number", "seat_number"})
        }
)
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "row_number", nullable = false)
    private Integer rowNumber;

    @Column(name = "seat_number", nullable = false)
    private Integer seatNumber;

    @ManyToOne(optional = false)
    @JoinColumn(name = "hall_id", nullable = false)
    @JsonIgnoreProperties({"name", "cinema"})
    private Hall hall;

    protected Seat() {
    }

    public Seat(Integer rowNumber, Integer seatNumber, Hall hall) {
        this.rowNumber = rowNumber;
        this.seatNumber = seatNumber;
        this.hall = hall;
    }

    public Long getId() {
        return id;
    }

    public Integer getRowNumber() {
        return rowNumber;
    }

    public void setRowNumber(Integer rowNumber) {
        this.rowNumber = rowNumber;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }

    public Hall getHall() {
        return hall;
    }

    public void setHall(Hall hall) {
        this.hall = hall;
    }
}
