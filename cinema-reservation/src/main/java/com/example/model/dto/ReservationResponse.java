package com.example.model.dto;

import com.example.model.ReservationStatus;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long id,
        Long customerId,
        Long screeningId,
        ReservationStatus status,
        LocalDateTime createdAt
) {}
