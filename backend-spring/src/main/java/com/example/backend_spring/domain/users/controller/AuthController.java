package com.example.backend_spring.domain.users.controller;

import com.example.backend_spring.domain.users.dto.UserLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserGeneralMessageResponse;
import com.example.backend_spring.domain.users.dto.UserCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;
import com.example.backend_spring.domain.users.dto.UserReminderUsernameRequest;
import com.example.backend_spring.domain.users.service.EmailService;
import com.example.backend_spring.domain.users.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/auth")
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
    public ResponseEntity<UserGeneralMessageResponse> remindUsername(@RequestBody @Valid UserReminderUsernameRequest dto) {
        // emailService.sendSimpleEmail(
        //         "fbio.alves095@gmail.com",
        //         "My subject",
        //         "My body with a bunch of non sense text"
        // );
        emailService.sendUsernameReminder(dto.email());
        return ResponseEntity.ok(
            new UserGeneralMessageResponse("Username reminder sent to " + dto.email())
        );
    }

    @PostMapping("/requestPasswordReset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid UserCreationRequestDTO dto) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserCreationRequestDTO dto) {
        return ResponseEntity.ok().build();
    }
}
