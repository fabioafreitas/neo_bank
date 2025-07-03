package com.example.backend_spring.domain.merchants.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MerchantResponseDTO(
		@NotNull UUID id,
		@NotBlank String storeName,
		@NotBlank String description
) {

}
