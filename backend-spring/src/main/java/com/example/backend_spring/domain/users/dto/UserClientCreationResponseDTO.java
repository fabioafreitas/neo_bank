package com.example.backend_spring.domain.users.dto;

import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;

import jakarta.validation.constraints.NotNull;

public record UserClientCreationResponseDTO(
    @NotNull AccountResponseDTO account
) {
}
