package com.example.backend_spring.domain.users.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.users.dto.UserProfileRequestDTO;
import com.example.backend_spring.domain.users.dto.UserProfileResponseDTO;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.model.UserProfile;
import com.example.backend_spring.domain.users.repository.UserProfileRepository;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

@Service
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final JwtTokenProviderService jwtTokenProviderService;

    public UserProfileService(UserProfileRepository userProfileRepository, JwtTokenProviderService jwtTokenProviderService) {
        this.userProfileRepository = userProfileRepository;
        this.jwtTokenProviderService = jwtTokenProviderService;
    }

    private UserProfile findCurrentUserProfile() {
        User user = jwtTokenProviderService.getContextUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        return userProfileRepository.findByUserId(user.getId())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found"));
    }

    public UserProfileResponseDTO getCurrentProfile() {
        return this.toDto(this.findCurrentUserProfile());
    }

    public UserProfileResponseDTO updateCurrentProfile(UserProfileRequestDTO dto) {
        UserProfile profile = this.findCurrentUserProfile();

        // Update fields only if they are not null
        // firstName is not allowed to be changed
        // lastName is not allowed to be changed
        // email is not allowed to be changed
        if (dto.phone() != null) profile.setPhone(dto.phone());
        if (dto.addressLine1() != null) profile.setAddressLine1(dto.addressLine1());
        if (dto.addressLine2() != null) profile.setAddressLine2(dto.addressLine2());
        if (dto.city() != null) profile.setCity(dto.city());
        if (dto.province() != null) profile.setProvince(dto.province());
        if (dto.postalCode() != null) profile.setPostalCode(dto.postalCode());
        if (dto.country() != null) profile.setCountry(dto.country());
        if (dto.profilePictureUrl() != null) profile.setProfilePictureUrl(dto.profilePictureUrl());

        // Save the updated profile to the database
        userProfileRepository.save(profile);

        // Convert the updated profile to a DTO and return it
        return toDto(profile);
    }

    public UserProfileResponseDTO toDto(UserProfile profile) {
        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        return new UserProfileResponseDTO(
            profile.getFirstName(),
            profile.getLastName(),
            profile.getEmail(),
            profile.getPhone(),
            profile.getAddressLine1(),
            profile.getAddressLine2(),
            profile.getCity(),
            profile.getProvince(),
            profile.getPostalCode(),
            profile.getCountry(),
            profile.getProfilePictureUrl()
        );
    }
}
