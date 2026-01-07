package com.example.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateMovieRequest(
        @NotBlank @Size(max = 150) String title,
        @NotNull @Min(1) Integer durationMinutes,
        @Size(max = 20) String rating,
        @Size(max = 500) String description
) {}
