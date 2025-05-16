package com.example.backend_spring.exception.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public record ErrorResponseDTO(
    @NotBlank LocalDateTime timestamp,
    @NotBlank int status,
    @NotBlank String error,
    @NotBlank String message,
    @NotBlank String path
) {
}