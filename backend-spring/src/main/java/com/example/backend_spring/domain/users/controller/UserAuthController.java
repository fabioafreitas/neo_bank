package com.example.backend_spring.domain.users.controller;

import com.example.backend_spring.domain.users.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.service.EmailService;
import com.example.backend_spring.domain.users.service.UserService;
import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/users/auth")
public class UserAuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/login")    
    public ResponseEntity<UserLoginResponseDTO> login(@RequestBody @Valid UserLoginRequestDTO dto) {
        return ResponseEntity.ok(userService.authenticate(dto.accessUsername(), dto.accessPassword()));
    }

    @PostMapping("/registerClient")
    public ResponseEntity<UserCreationResponseDTO> registerClient(
            @RequestBody @Valid UserCreationClientRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.registerUserClient(dto));
    }

    @PostMapping("/registerMerchant")
    public ResponseEntity<UserCreationResponseDTO> registerMerchant(
            @RequestBody @Valid UserCreationMerchantRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.registerUserMerchant(dto));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/registerAdmin")
    public ResponseEntity<UserCreationResponseDTO> registerAdmin(
            @RequestBody @Valid UserCreationAdminRequestDTO dto
    ) {
        return ResponseEntity.ok(userService.registerUserAdmin(dto));
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
