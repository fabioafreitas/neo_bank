package com.example.backend_spring.domain.users.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserProfileResponseDTO(
    String firstName,
    String lastName,
    String email,
    String phone,
    String addressLine1,
    String addressLine2,
    String city,
    String province,
    String postalCode,
    String country,
    String profilePictureUrl
) {
}