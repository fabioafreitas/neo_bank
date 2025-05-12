package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AccountCreationDTO(
        @NotBlank String name,
        String description,
        @NotNull BigDecimal price
) {
}