package com.example.backend_spring.domain.users.dto;

import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.merchants.dto.MerchantResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCreationResponseDTO(
	@NotNull UserProfileResponseDTO userProfile,
	AccountResponseDTO account,
	MerchantResponseDTO merchant
) {
}
