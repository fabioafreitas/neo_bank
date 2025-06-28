package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.users.model.User;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountCreationDTO(
    @NotNull User user,
    @NotBlank BigDecimal balance,
    @NotNull AccountStatus status,
    @NotBlank String transactionPassword
) {
}