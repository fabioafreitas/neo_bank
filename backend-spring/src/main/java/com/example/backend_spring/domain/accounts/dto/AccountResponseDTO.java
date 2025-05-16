package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.backend_spring.domain.accounts.AccountStatus;
import com.example.backend_spring.domain.accounts.AccountType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountResponseDTO(
    @NotBlank UUID userId,
    @NotBlank UUID accountId,
    @NotBlank BigDecimal balance,
    @NotNull AccountType type,
    @NotNull AccountStatus status,
    @NotBlank String accountNumber
) {
}