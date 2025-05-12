package com.example.backend_spring.domain.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "products")
@Entity(name = "products")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Setter private String name;
    @Setter private String description;
    @Setter private BigDecimal price;

    @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Setter private OffsetDateTime createdAt = OffsetDateTime.now();

    
    @Column(name = "updated_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    @Setter private OffsetDateTime updatedAt = OffsetDateTime.now();
}