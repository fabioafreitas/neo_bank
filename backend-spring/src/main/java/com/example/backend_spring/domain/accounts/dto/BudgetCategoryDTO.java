package com.example.backend_spring.domain.accounts.dto;

import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BudgetCategoryDTO(
    @NotBlank(message = "Budget category name is required")
    String budgetCategoryName,

    @NotNull(message = "Budget category ID is required")
    UUID budgetCategoryId
) {}