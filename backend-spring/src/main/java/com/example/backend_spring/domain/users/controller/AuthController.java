package com.example.backend_spring.domain.users.controller;

import com.example.backend_spring.domain.users.dto.UserLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponseDTO;
import com.example.backend_spring.domain.users.dto.UserCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;
import com.example.backend_spring.domain.users.dto.UserRecoverCredentialsRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResetAccessPasswordRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResetTransactionPasswordRequestDTO;
import com.example.backend_spring.domain.users.service.EmailService;
import com.example.backend_spring.domain.users.service.UserService;
import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")    
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto) {
        return ResponseEntity.ok(userService.authenticate(dto.accessUsername(), dto.accessPassword()));
    }

    @PostMapping("/registerClient")
    public ResponseEntity<UserCreationResponseDTO> register(@RequestBody @Valid UserCreationRequestDTO dto) {
        return ResponseEntity.ok(userService.registerUserClient(dto));
    }

    @PostMapping("/remindUsername")
    public ResponseEntity<UserGeneralMessageResponseDTO> remindUsername(@RequestBody @Valid UserRecoverCredentialsRequestDTO dto) {
        return ResponseEntity.ok(
            emailService.sendUsernameReminder(dto.email())
        );
    }

    @PostMapping("/requestAccessPasswordReset")
    public ResponseEntity<UserGeneralMessageResponseDTO> requestAccessPasswordReset(@RequestBody @Valid UserRecoverCredentialsRequestDTO dto) {
        return ResponseEntity.ok(
            userService.requestPasswordReset(
                dto.email(), 
                PasswordResetRequestType.LOGIN_PASSWORD
            )
        );
    }

    // Only auth clients can access this endpoint
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/requestTransactionPasswordReset")
    public ResponseEntity<UserGeneralMessageResponseDTO> requestTransactionPasswordReset(@RequestBody @Valid UserRecoverCredentialsRequestDTO dto) {
        return ResponseEntity.ok(
            userService.requestPasswordReset(
                dto.email(), 
                PasswordResetRequestType.TRANSACTION_PASSWORD
            )
        );
    }

    @PostMapping("/resetAccessPassword")
    public ResponseEntity<UserGeneralMessageResponseDTO> resetAccessPassword(@RequestBody @Valid UserResetAccessPasswordRequestDTO dto) {
        return ResponseEntity.ok(userService.resetAccessPassword(dto));
    }

    // Only auth clients can access this endpoint
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/resetTransactionPassword")
    public ResponseEntity<UserGeneralMessageResponseDTO> resetTransactionPassword(@RequestBody @Valid UserResetTransactionPasswordRequestDTO dto) {
        return ResponseEntity.ok(userService.resetTransactionPassword(dto));
    }
}
