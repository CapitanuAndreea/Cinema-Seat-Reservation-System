package com.example.controller;

import com.example.model.dto.CreateSeatRequest;
import com.example.model.dto.SeatResponse;
import com.example.service.SeatService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/seats")
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping
    public ResponseEntity<List<SeatResponse>> getAll() {
        return ResponseEntity.ok(seatService.findAll());
    }

    @GetMapping("/by-hall/{hallId}")
    public ResponseEntity<List<SeatResponse>> getByHall(@PathVariable Long hallId) {
        return ResponseEntity.ok(seatService.findByHall(hallId));
    }

    @PostMapping
    public ResponseEntity<SeatResponse> create(@RequestBody @Valid CreateSeatRequest request) {
        SeatResponse response = seatService.addSeat(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
