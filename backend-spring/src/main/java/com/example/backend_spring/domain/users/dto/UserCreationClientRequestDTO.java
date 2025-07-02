package com.example.backend_spring.domain.users.dto;

import com.example.backend_spring.utils.PatternUtils;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserCreationClientRequestDTO(
    @NotNull @Valid UserCreationBasicInfoRequestDTO userInfo,
    @NotNull @Valid AccountCreationInfoRequestDTO accountInfo
    ) {
    public record AccountCreationInfoRequestDTO(
            @NotBlank
            @Pattern(
                    regexp = PatternUtils.TRANSACTION_PASSWORD_PATTERN,
                    message = "Invalid transaction password, must be 6 numeric digits")
            String transactionPassword
    ) {}
}
