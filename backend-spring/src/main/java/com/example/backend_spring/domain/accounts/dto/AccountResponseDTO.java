package com.example.backend_spring.domain.accounts.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.products.Product;

public record AccountResponseDTO(
        UUID id, 
        String name,
        String description,
        BigDecimal price,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
    ) {
    public AccountResponseDTO(Product product){
        this(
            product.getId(), 
            product.getName(), 
            product.getDescription(),
            product.getPrice(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}