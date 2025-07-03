package com.example.backend_spring.domain.merchants.dto;

import jakarta.validation.constraints.NotBlank;

public record MerchantRequestDTO(
		String storeName,
		String description
) {

}
