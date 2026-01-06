package com.example.controller;

import com.example.model.Cinema;
import com.example.repository.CinemaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaRepository cinemaRepository;

    public CinemaController(CinemaRepository cinemaRepository) {
        this.cinemaRepository = cinemaRepository;
    }

    @GetMapping
    public List<Cinema> getAll() {
        return cinemaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cinema getById(@PathVariable Long id) {
        return cinemaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cinema not found with id=" + id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Cinema create(@RequestBody Cinema cinema) {
        return cinemaRepository.save(cinema);
    }
}
