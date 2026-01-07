package com.example.controller;

import com.example.model.dto.CinemaResponse;
import com.example.model.dto.CreateCinemaRequest;
import com.example.service.CinemaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cinemas")
public class CinemaController {

    private final CinemaService cinemaService;

    public CinemaController(CinemaService cinemaService) {
        this.cinemaService = cinemaService;
    }

    @GetMapping
    public ResponseEntity<List<CinemaResponse>> getAll() {
        return ResponseEntity.ok(cinemaService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CinemaResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(cinemaService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CinemaResponse> create(@RequestBody @Valid CreateCinemaRequest request) {
        CinemaResponse response = cinemaService.add(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
