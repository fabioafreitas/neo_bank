package com.example.backend_spring.domain.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.management.service.ManagementService;

@RestController
@RequestMapping("/api/management")
public class ManagementController {

    @Autowired
    private ManagementService managementService;

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public ResponseEntity<List<?>> getAllUsers(Pageable pageable) {
        return ResponseEntity.ok(managementService.findAllUsers(pageable));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts")
    public ResponseEntity<List<?>> getAllAccounts(Pageable pageable) {
        return ResponseEntity.ok(managementService.findAllAccounts(pageable));
    }
}