package com.example.controller;

import com.example.model.Cinema;
import com.example.model.Hall;
import com.example.repository.CinemaRepository;
import com.example.repository.HallRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    private final HallRepository hallRepository;
    private final CinemaRepository cinemaRepository;

    public HallController(HallRepository hallRepository, CinemaRepository cinemaRepository) {
        this.hallRepository = hallRepository;
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping
    public List<Hall> getAll() {
        return hallRepository.findAll();
    }

    @GetMapping("/{id}")
    public Hall getById(@PathVariable Long id) {
        return hallRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hall not found with id=" + id));
    }

    @GetMapping("/by-cinema/{cinemaId}")
    public List<Hall> getByCinema(@PathVariable Long cinemaId) {
        return hallRepository.findByCinemaId(cinemaId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Hall create(@RequestBody CreateHallRequest request) {
        Cinema cinema = cinemaRepository.findById(request.cinemaId())
                .orElseThrow(() -> new RuntimeException("Cinema not found with id=" + request.cinemaId()));

        Hall hall = new Hall(request.name(), cinema);
        return hallRepository.save(hall);
    }

    public record CreateHallRequest(String name, Long cinemaId) {}
}
