package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import com.example.backend_spring.domain.accounts.AccountStatus;
import com.example.backend_spring.domain.accounts.AccountType;

public record AccountUpdateDTO(
    BigDecimal balance,
    AccountType type,
    AccountStatus status
) {
}