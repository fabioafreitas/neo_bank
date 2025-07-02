package com.example.backend_spring.domain.users.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreationMerchantRequestDTO(
        @NotNull @Valid UserCreationBasicInfoRequestDTO userInfo,
        @NotNull @Valid MerchantCreationInfoRequestDTO merchantInfo
) {
    public record MerchantCreationInfoRequestDTO(
            @NotBlank String storeName,
            @NotBlank String description
    ) {}
}
