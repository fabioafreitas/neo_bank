package com.example.backend_spring.domain.users.dto;

import java.util.UUID;

import com.example.backend_spring.utils.PatternUtils;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record UserResetAccessPasswordRequestDTO(
    @NotBlank
    @Pattern(
            regexp = PatternUtils.ACCESS_PASSWORD_PATTERN,
            message = "Invalid access password, must have at least 8 digits, one uppercase letter, one lowercase letter, one special character and one number."
    )
    String newAccessPassword,
    @NotNull UUID resetRequestToken
    ) {
}
