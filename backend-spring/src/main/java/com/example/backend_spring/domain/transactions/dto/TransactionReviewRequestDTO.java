package com.example.backend_spring.domain.transactions.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record TransactionReviewRequestDTO(
		@NotNull
		UUID transactionNumber,

		@Size(min = 10, max = 255, message = "A mensagem de rejeição deve ter entre 10 e 255 caracteres")
		String rejectionMessage
) {}