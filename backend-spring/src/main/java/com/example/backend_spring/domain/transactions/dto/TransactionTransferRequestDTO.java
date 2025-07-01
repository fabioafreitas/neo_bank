package com.example.backend_spring.domain.transactions.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TransactionTransferRequestDTO(
    @NotNull TransactionRequestDTO sourceTransaction,
    @NotBlank String transferAccountNumber
) {
}
