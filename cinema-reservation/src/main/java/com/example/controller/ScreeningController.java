package com.example.controller;

import com.example.model.dto.CreateScreeningRequest;
import com.example.model.dto.ScreeningResponse;
import com.example.service.ScreeningService;
import com.example.model.dto.SeatAvailabilityResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    public ScreeningController(ScreeningService screeningService) {
        this.screeningService = screeningService;
    }

    @GetMapping
    public ResponseEntity<List<ScreeningResponse>> getAll() {
        return ResponseEntity.ok(screeningService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ScreeningResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(screeningService.findById(id));
    }

    @GetMapping("/by-movie/{movieId}")
    public ResponseEntity<List<ScreeningResponse>> getByMovie(@PathVariable Long movieId) {
        return ResponseEntity.ok(screeningService.findByMovie(movieId));
    }

    @GetMapping("/by-hall/{hallId}")
    public ResponseEntity<List<ScreeningResponse>> getByHall(@PathVariable Long hallId) {
        return ResponseEntity.ok(screeningService.findByHall(hallId));
    }

    @GetMapping("/{id}/seats")
    public ResponseEntity<List<SeatAvailabilityResponse>> seatMap(@PathVariable Long id) {
        return ResponseEntity.ok(screeningService.getSeatMap(id));
    }

    @PostMapping
    public ResponseEntity<ScreeningResponse> create(@RequestBody @Valid CreateScreeningRequest request) {
        ScreeningResponse response = screeningService.addScreening(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
