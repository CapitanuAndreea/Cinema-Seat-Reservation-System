package com.example.model.dto;

public record SeatResponse(
        Long id,
        Integer rowNumber,
        Integer seatNumber,
        Long hallId
) {}
