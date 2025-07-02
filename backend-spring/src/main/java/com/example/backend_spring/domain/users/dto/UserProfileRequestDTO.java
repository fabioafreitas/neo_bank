package com.example.backend_spring.domain.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserProfileRequestDTO(
    @Email(message = "Email inválido")
    String email,
    
    @Pattern(
        regexp = "^(\\+55\\s?)?(\\(?\\d{2}\\)?\\s?)?(9\\d{4}|[2-9]\\d{3})[-\\s]?\\d{4}$",
        message = "Número de telefone inválido")
    String phone,

    String addressLine1,
    String addressLine2,
    String city,
    String province,

    @Pattern(
        regexp = "^\\d{5}-\\d{3}$",
        message = "CEP inválido. Exemplo: 04063-002"
    )
    String postalCode,
    String country,

    @Pattern(
            regexp = "^(http|https)://.*\\.(png|jpg|jpeg|webp|gif)(\\?.*)?$",
            message = "URL da foto de perfil deve ser uma URL válida para imagem"
    )
    String profilePictureUrl
) {
}