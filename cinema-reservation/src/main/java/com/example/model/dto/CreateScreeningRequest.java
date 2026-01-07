package com.example.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateScreeningRequest(
        @NotNull Long movieId,
        @NotNull Long hallId,
        @NotNull LocalDateTime startTime,
        @NotNull @DecimalMin("0.00") BigDecimal price
) {}
