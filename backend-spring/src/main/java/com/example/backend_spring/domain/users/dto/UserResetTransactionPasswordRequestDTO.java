package com.example.backend_spring.domain.users.dto;

import java.util.UUID;

import com.example.backend_spring.utils.PatternUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserResetTransactionPasswordRequestDTO(
    @NotBlank
    String accessPassword,

    @NotBlank
    @Pattern(
            regexp = PatternUtils.TRANSACTION_PASSWORD_PATTERN,
            message = "Invalid transaction password, must be 6 numeric digits")
    String newTransactionPassword,

    @NotNull UUID resetRequestToken
    ) {
}
