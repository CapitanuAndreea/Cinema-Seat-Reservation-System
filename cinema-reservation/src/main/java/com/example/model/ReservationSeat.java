package com.example.model;

import jakarta.persistence.*;

@Entity
@Table(
        name = "reservation_seats",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_screening_seat", columnNames = {"screening_id", "seat_id"})
        }
)
public class ReservationSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "reservation_id", nullable = false)
    private Reservation reservation;

    @ManyToOne(optional = false)
    @JoinColumn(name = "screening_id", nullable = false)
    private Screening screening;

    @ManyToOne(optional = false)
    @JoinColumn(name = "seat_id", nullable = false)
    private Seat seat;

    protected ReservationSeat() {}

    public ReservationSeat(Reservation reservation, Screening screening, Seat seat) {
        this.reservation = reservation;
        this.screening = screening;
        this.seat = seat;
    }

    public Long getId() { return id; }

    public Reservation getReservation() { return reservation; }

    public Screening getScreening() { return screening; }

    public Seat getSeat() { return seat; }
}
