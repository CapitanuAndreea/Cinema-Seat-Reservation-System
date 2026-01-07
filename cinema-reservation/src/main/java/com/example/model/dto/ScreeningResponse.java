package com.example.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ScreeningResponse(
        Long id,
        Long movieId,
        String movieTitle,
        Integer durationMinutes,
        Long hallId,
        String hallName,
        Long cinemaId,
        LocalDateTime startTime,
        BigDecimal price
) {}
