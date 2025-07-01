package com.example.backend_spring.domain.transactions.dto;

import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.transactions.utils.TransactionOperationType;
import com.example.backend_spring.domain.transactions.utils.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransactionTransferResponseDTO(
		@NotBlank TransactionResponseDTO transaction,
		@NotBlank TransferReceiverInfoDTO receiverInfo
) {

}
