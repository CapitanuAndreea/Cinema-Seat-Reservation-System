package com.example.controller;

import com.example.model.dto.CreateHallRequest;
import com.example.model.dto.HallResponse;
import com.example.service.HallService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/halls")
public class HallController {

    private final HallService hallService;

    public HallController(HallService hallService) {
        this.hallService = hallService;
    }

    @GetMapping
    public ResponseEntity<List<HallResponse>> getAll() {
        return ResponseEntity.ok(hallService.findAll());
    }

    @GetMapping("/by-cinema/{cinemaId}")
    public ResponseEntity<List<HallResponse>> getByCinema(@PathVariable Long cinemaId) {
        return ResponseEntity.ok(hallService.findByCinema(cinemaId));
    }

    @PostMapping
    public ResponseEntity<HallResponse> create(@RequestBody @Valid CreateHallRequest request) {
        HallResponse response = hallService.addHall(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
