package com.example.backend_spring.domain.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend_spring.domain.users.dto.UserCreationDTO;
import com.example.backend_spring.domain.users.dto.UserRequestDTO;
import com.example.backend_spring.domain.users.dto.UserResponseDTO;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")    
    public ResponseEntity<UserResponseDTO> login(@RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.ok(userService.authenticate(dto.username(), dto.password()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserCreationDTO> register(@RequestBody @Valid UserRequestDTO dto) {
        return ResponseEntity.ok(userService.registerUser(dto));
    }
    
}
