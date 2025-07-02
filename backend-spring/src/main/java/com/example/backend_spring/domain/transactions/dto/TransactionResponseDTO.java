package com.example.backend_spring.domain.transactions.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;


import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.transactions.utils.TransactionOperationType;
import com.example.backend_spring.domain.transactions.utils.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionResponseDTO (
    @NotBlank UUID transactionNumber,
	@NotBlank OffsetDateTime createdAt,
	@NotBlank TransactionOperationType operationType,
	@NotBlank TransactionStatus status,
	@NotNull String description,
	@NotNull BigDecimal amount,
	String rejectionMessage,
	BudgetCategoryDTO budgetCategory
) {
}
