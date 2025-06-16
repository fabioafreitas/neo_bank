package com.example.backend_spring.domain.users.dto;

import jakarta.validation.constraints.Email;

public record UserRecoverCredentialsRequestDTO(
    @Email String email
    ) {
}
