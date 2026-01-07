package com.example.model.dto;

public record MovieResponse(
        Long id,
        String title,
        Integer durationMinutes,
        String rating,
        String description
) {}
