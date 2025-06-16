package com.example.backend_spring.domain.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserGeneralMessageResponseDTO(
    @NotBlank String message
    ) {
}
