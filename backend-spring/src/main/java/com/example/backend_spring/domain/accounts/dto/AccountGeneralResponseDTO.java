package com.example.backend_spring.domain.accounts.dto;

import jakarta.validation.constraints.NotBlank;

public record AccountGeneralResponseDTO(
    @NotBlank String message
) {
}
