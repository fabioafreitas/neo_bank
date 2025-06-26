package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.accounts.utils.AccountType;

public record AccountUpdateDTO(
    BigDecimal balance,
    AccountType type,
    AccountStatus status,
    String transactionPassword
) {
}