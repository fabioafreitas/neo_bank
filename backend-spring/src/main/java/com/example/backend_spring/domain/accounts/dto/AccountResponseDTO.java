package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.accounts.utils.AccountType;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountResponseDTO(
    UUID userId,
    @NotBlank UUID accountId,
    @NotBlank BigDecimal balance,
    @NotNull AccountType type,
    @NotNull AccountStatus status,
    @NotBlank String accountNumber
) {
}