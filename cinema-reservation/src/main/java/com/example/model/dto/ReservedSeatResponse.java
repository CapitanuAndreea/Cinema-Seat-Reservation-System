package com.example.model.dto;

public record ReservedSeatResponse(
        Long seatId,
        Integer rowNumber,
        Integer seatNumber
) {}
