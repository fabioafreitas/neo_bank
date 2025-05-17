package com.example.backend_spring.domain.transactions.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public record TransactionRequestDTO (
    @NotBlank BigDecimal amount,
    @NotBlank String description,
    String transferAccountNumber
) {
}
