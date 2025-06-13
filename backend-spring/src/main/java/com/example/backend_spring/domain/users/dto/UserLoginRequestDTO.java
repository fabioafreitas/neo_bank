package com.example.backend_spring.domain.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginRequestDTO(
    @NotBlank String accessUsername,
    @NotBlank String accessPassword
    ) {
}
