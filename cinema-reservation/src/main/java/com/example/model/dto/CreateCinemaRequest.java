package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateCinemaRequest(
        @NotBlank @Size(max = 100) String name,
        @NotBlank @Size(max = 100) String city
) {}
