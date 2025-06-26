package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record BudgetAllocationResponseDTO(
    UUID id,
    UUID accountId,
    UUID budgetCategoryId,
    String budgetCategoryName,
    BigDecimal allocationValue
) {}