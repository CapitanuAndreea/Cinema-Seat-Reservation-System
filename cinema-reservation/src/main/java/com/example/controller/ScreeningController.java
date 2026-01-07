package com.example.controller;

import com.example.model.Hall;
import com.example.model.Movie;
import com.example.model.Screening;
import com.example.repository.HallRepository;
import com.example.repository.MovieRepository;
import com.example.repository.ScreeningRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    private final ScreeningRepository screeningRepository;
    private final MovieRepository movieRepository;
    private final HallRepository hallRepository;

    public ScreeningController(ScreeningRepository screeningRepository,
                               MovieRepository movieRepository,
                               HallRepository hallRepository) {
        this.screeningRepository = screeningRepository;
        this.movieRepository = movieRepository;
        this.hallRepository = hallRepository;
    }

    @GetMapping
    public List<Screening> getAll() {
        return screeningRepository.findAll();
    }

    @GetMapping("/{id}")
    public Screening getById(@PathVariable Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Screening not found with id=" + id));
    }

    @GetMapping("/by-movie/{movieId}")
    public List<Screening> getByMovie(@PathVariable Long movieId) {
        return screeningRepository.findByMovieId(movieId);
    }

    @GetMapping("/by-hall/{hallId}")
    public List<Screening> getByHall(@PathVariable Long hallId) {
        return screeningRepository.findByHallId(hallId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Screening create(@RequestBody CreateScreeningRequest request) {
        Movie movie = movieRepository.findById(request.movieId())
                .orElseThrow(() -> new RuntimeException("Movie not found with id=" + request.movieId()));

        Hall hall = hallRepository.findById(request.hallId())
                .orElseThrow(() -> new RuntimeException("Hall not found with id=" + request.hallId()));

        Screening screening = new Screening(movie, hall, request.startTime(), request.price());
        return screeningRepository.save(screening);
    }

    public record CreateScreeningRequest(Long movieId, Long hallId, LocalDateTime startTime, BigDecimal price) {}
}
