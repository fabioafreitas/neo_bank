package com.example.backend_spring.domain.merchants.controller;

import java.util.List;
import java.util.UUID;

import com.example.backend_spring.domain.merchants.dto.MerchantProfileResponseDTO;
import com.example.backend_spring.domain.merchants.dto.MerchantRequestDTO;
import com.example.backend_spring.domain.merchants.dto.MerchantResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.merchants.service.MerchantService;

@RestController
@RequestMapping("/api/merchants")
public class MerchantController {

    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PreAuthorize("hasRole('MERCHANT')")
    @GetMapping("/me")
    public ResponseEntity<MerchantProfileResponseDTO> getCurrentMerchantProfile() {
        return ResponseEntity.ok(merchantService.getCurrentMerchantProfile());
    }

    @PreAuthorize("hasRole('MERCHANT')")
    @PutMapping("/me")
    public ResponseEntity<MerchantProfileResponseDTO> updateCurrentMerchantProfile(@RequestBody @Valid MerchantRequestDTO dto) {
        return ResponseEntity.ok(merchantService.updateCurrentMerchantProfile(
                dto
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<MerchantProfileResponseDTO>> getAllMerchants() {
        return ResponseEntity.ok(merchantService.getAllMerchants());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{merchantId}")
    public ResponseEntity<MerchantProfileResponseDTO> getMerchantById(@PathVariable("merchantId") UUID merchantId) {
        return ResponseEntity.ok(merchantService.getMerchantById(merchantId));
    }
}