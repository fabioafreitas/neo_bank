package com.example.backend_spring.domain.transactions.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record TransferReceiverInfoDTO(
		@NotNull String accountNumber,
		@NotNull String firstName,
		@NotNull String lastName
) {}