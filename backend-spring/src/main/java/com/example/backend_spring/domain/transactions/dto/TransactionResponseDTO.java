package com.example.backend_spring.domain.transactions.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.transactions.utils.TransactionType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionResponseDTO (
    @NotBlank UUID transactionNumber,
    @NotNull TransactionType type,
    @NotBlank boolean isSuccess,
    @NotBlank BigDecimal amount,
    @NotBlank OffsetDateTime createdAt,
    @NotBlank String description
) {
}
