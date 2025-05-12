package com.example.backend_spring.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResponseDTO;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private JwtTokenProviderService tokenService;

    @Autowired
    private UserService userService;

    @PostMapping("/login")    
    public ResponseEntity<?> login(@RequestBody @Valid UserRequestDTO dto) {
        return userService.authenticate(dto.username(), dto.password())
                .map(user -> {
                String token = tokenService.generateToken(user.getUsername(), user.getRole().getRole());
                return ResponseEntity.ok(new UserResponseDTO(token));
            })
            .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRequestDTO dto) {
        userService.registerUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    
}
