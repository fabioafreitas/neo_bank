package com.example.backend_spring.domain.users.dto;

import jakarta.validation.constraints.NotBlank;

public record UserCreationRequestDTO(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String email,
    @NotBlank String accessUsername,
    @NotBlank String accessPassword,
    String transactionPassword
    ) {
}
