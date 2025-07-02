package com.example.backend_spring.domain.merchants.dto;

import jakarta.validation.constraints.NotBlank;

public record MerchantResponseDTO(
		@NotBlank String storeName,
		@NotBlank String description
) {

}
