package com.example.model.dto;

public record CustomerResponse(
        Long id,
        String fullName,
        String email
) {}
