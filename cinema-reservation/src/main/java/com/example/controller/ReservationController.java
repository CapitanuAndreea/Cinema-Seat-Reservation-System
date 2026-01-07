package com.example.controller;

import com.example.model.Reservation;
import com.example.repository.ReservationRepository;
import com.example.repository.ReservationSeatRepository;
import com.example.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationRepository reservationRepository;
    private final ReservationSeatRepository reservationSeatRepository;

    public ReservationController(ReservationService reservationService,
                                 ReservationRepository reservationRepository,
                                 ReservationSeatRepository reservationSeatRepository) {
        this.reservationService = reservationService;
        this.reservationRepository = reservationRepository;
        this.reservationSeatRepository = reservationSeatRepository;
    }

    @GetMapping("/{id}")
    public Reservation getById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reservation not found with id=" + id));
    }

    @GetMapping("/{id}/seats")
    public List<?> getReservedSeats(@PathVariable Long id) {
        return reservationSeatRepository.findByReservationId(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Reservation create(@RequestBody CreateReservationRequest request) {
        return reservationService.createReservation(request.customerId(), request.screeningId(), request.seatIds());
    }

    @PutMapping("/{id}/cancel")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(@PathVariable Long id) {
        reservationService.cancelReservation(id);
    }

    public record CreateReservationRequest(Long customerId, Long screeningId, List<Long> seatIds) {}
}
