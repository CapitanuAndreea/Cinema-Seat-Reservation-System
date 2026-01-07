package com.example.controller;

import com.example.model.Hall;
import com.example.model.Seat;
import com.example.repository.HallRepository;
import com.example.repository.SeatRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatRepository seatRepository;
    private final HallRepository hallRepository;

    public SeatController(SeatRepository seatRepository, HallRepository hallRepository) {
        this.seatRepository = seatRepository;
        this.hallRepository = hallRepository;
    }

    @GetMapping
    public List<Seat> getAll() {
        return seatRepository.findAll();
    }

    @GetMapping("/by-hall/{hallId}")
    public List<Seat> getByHall(@PathVariable Long hallId) {
        return seatRepository.findByHallId(hallId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Seat create(@RequestBody CreateSeatRequest request) {
        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new RuntimeException("Hall not found with id=" + request.hallId()));

        Seat seat = new Seat(request.rowNumber(), request.seatNumber(), hall);
        return seatRepository.save(seat);
    }

    public record CreateSeatRequest(Integer rowNumber, Integer seatNumber, Long hallId) {}
}
