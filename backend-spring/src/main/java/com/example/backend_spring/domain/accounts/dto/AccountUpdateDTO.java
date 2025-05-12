package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;

public record AccountUpdateDTO(
        String name,
        String description,
        BigDecimal price
) {
}