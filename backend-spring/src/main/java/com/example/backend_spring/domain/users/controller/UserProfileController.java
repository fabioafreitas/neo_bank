package com.example.backend_spring.domain.users.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserProfileRequestDTO;
import com.example.backend_spring.domain.users.dto.UserProfileResponseDTO;
import com.example.backend_spring.domain.users.service.UserProfileService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/profile")
public class UserProfileController {

    private final UserProfileService userProfileService;

    public UserProfileController(UserProfileService userProfileService) {
        this.userProfileService = userProfileService;
    }

    @PreAuthorize("hasAnyRole('CLIENT', 'MERCHANT')")
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> getCurrentUserProfile() {
        return ResponseEntity.ok(userProfileService.getCurrentProfile());
    }

    @PreAuthorize("hasAnyRole('CLIENT', 'MERCHANT')")
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponseDTO> updateCurrentUserProfile(@RequestBody @Valid UserProfileRequestDTO dto) {
        return ResponseEntity.ok(userProfileService.updateCurrentProfile(dto));
    }
}
