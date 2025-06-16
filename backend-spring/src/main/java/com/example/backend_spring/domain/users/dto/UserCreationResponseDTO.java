package com.example.backend_spring.domain.users.dto;

import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserCreationResponseDTO(
    AccountResponseDTO account
) {
}
