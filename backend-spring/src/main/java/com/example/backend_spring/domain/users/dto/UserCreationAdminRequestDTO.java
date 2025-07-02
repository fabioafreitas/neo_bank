package com.example.backend_spring.domain.users.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record UserCreationAdminRequestDTO(
    @NotNull @Valid UserCreationBasicInfoRequestDTO userInfo
    ) {
}
