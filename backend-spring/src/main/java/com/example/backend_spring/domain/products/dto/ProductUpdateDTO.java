package com.example.backend_spring.domain.products.dto;

import java.math.BigDecimal;

public record ProductUpdateDTO(
        String name,
        String description,
        BigDecimal price
) {
}