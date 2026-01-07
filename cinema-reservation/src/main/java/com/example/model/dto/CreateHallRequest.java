package com.example.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateHallRequest(
        @NotBlank @Size(max = 50) String name,
        @NotNull Long cinemaId
) {}
