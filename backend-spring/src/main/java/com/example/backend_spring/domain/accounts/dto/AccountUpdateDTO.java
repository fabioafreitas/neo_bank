package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;

public record AccountUpdateDTO(
    BigDecimal balance,
    AccountStatus status,
    String transactionPassword
) {
}