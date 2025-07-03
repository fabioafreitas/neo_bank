package com.example.backend_spring.domain.merchants.dto;

import com.example.backend_spring.domain.users.dto.UserProfileResponseDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MerchantProfileResponseDTO(
		@NotNull MerchantResponseDTO merchant,
		@NotNull UserProfileResponseDTO userProfile
) {

}
