package com.example.backend_spring.domain.merchants.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.merchants.service.MerchantService;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    @Autowired
    private MerchantService merchantService;

    // TODO review
    @PreAuthorize("hasRole('MERCHANT')")
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentMerchantProfile() {
        return ResponseEntity.ok(merchantService.findByJwt());
    }

    // TODO review
    @PreAuthorize("hasRole('MERCHANT')")
    @PutMapping("/me")
    public ResponseEntity<?> updateCurrentMerchantProfile(@RequestBody Object dto) {
        return ResponseEntity.ok(merchantService.updateMerchantProfile(dto));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<?>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.findAll());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{merchantId}")
    public ResponseEntity<?> getMerchantById(@PathVariable("merchantId") UUID merchantId) {
        return ResponseEntity.ok(merchantService.findById(merchantId));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{merchantId}")
    public ResponseEntity<?> deactivateMerchant(@PathVariable("merchantId") UUID merchantId) {
        merchantService.deactivateMerchant(merchantId);
        return ResponseEntity.ok().build();
    }
}