package com.example.backend_spring.domain.users.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResetTransactionPasswordRequestDTO(
    @NotBlank String accessPassword,
    @NotBlank String newTransactionPassword,
    @NotNull UUID resetRequestToken
    ) {
}
