package com.example.backend_spring.domain.users.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserLoginRequestDTO;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/users/")
public class UserProfileController {
    // TODO
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/profile")    
    public ResponseEntity<?> getPurrentUserProfile(@RequestBody @Valid UserLoginRequestDTO dto) {
        return ResponseEntity.ok().build();
    }

    // TODO
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/me/profile")    
    public ResponseEntity<?> updatePurrentUserProfile(@RequestBody @Valid UserLoginRequestDTO dto) {
        return ResponseEntity.ok().build();
    }
}
