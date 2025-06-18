package com.example.backend_spring.domain.users.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResetAccessPasswordRequestDTO(
    @NotBlank String newAccessPassword,
    @NotNull UUID resetRequestToken
    ) {
}
