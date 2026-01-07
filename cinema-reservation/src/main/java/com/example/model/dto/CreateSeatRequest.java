package com.example.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record CreateSeatRequest(
        @NotNull @Min(1) Integer rowNumber,
        @NotNull @Min(1) Integer seatNumber,
        @NotNull Long hallId
) {}
