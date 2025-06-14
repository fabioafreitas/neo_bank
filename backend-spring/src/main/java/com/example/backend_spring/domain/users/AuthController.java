package com.example.backend_spring.domain.users;

import com.example.backend_spring.domain.users.dto.UserLoginRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserClientCreationResponseDTO;
import com.example.backend_spring.domain.users.dto.UserClientCreationRequestDTO;
import com.example.backend_spring.domain.users.dto.UserLoginResponseDTO;

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
    public ResponseEntity<UserClientCreationResponseDTO> register(@RequestBody @Valid UserClientCreationRequestDTO dto) {
        return ResponseEntity.ok(userService.registerUserClient(dto));
    }

    @PostMapping("/remindUsername")
    public ResponseEntity<?> remindUsername(@RequestBody @Valid UserClientCreationRequestDTO dto) {
        emailService.sendSimpleEmail(
                "fbio.alves095@gmail.com",
                "My subject",
                "My body with a bunch of non sense text"
        );
        return ResponseEntity.ok().build();
    }

    @PostMapping("/requestPasswordReset")
    public ResponseEntity<?> requestPasswordReset(@RequestBody @Valid UserClientCreationRequestDTO dto) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<?> resetPassword(@RequestBody @Valid UserClientCreationRequestDTO dto) {
        return ResponseEntity.ok().build();
    }
}
