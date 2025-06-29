package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountResponseDTO(
    @NotBlank String ownerFirstName,
    @NotBlank String ownerLastName,
    @NotBlank String ownerEmail,
    @NotBlank BigDecimal balance,
    @NotNull AccountStatus status,
    @NotBlank String accountNumber
) {
}