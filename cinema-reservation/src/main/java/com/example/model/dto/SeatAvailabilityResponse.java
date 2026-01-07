package com.example.model.dto;

public record SeatAvailabilityResponse(
        Long seatId,
        Integer rowNumber,
        Integer seatNumber,
        boolean reserved
) {}
