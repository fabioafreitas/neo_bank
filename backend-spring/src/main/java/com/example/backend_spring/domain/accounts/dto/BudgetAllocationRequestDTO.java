package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;
import java.util.UUID;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record BudgetAllocationRequestDTO(
    @NotNull(message = "Budget category ID is required")
    UUID budgetCategoryId,
    
    @NotNull(message = "Allocation value is required")
    @DecimalMin(value = "0.0", inclusive = true, message = "Allocation value must be greater than or equal to 0")
    BigDecimal allocationValue
) {}