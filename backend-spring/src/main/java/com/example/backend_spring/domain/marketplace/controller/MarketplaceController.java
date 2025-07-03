package com.example.backend_spring.domain.marketplace.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.management.service.ManagementService;

@RestController
@RequestMapping("/api/marketplace")
public class MarketplaceController {

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/merchants")
    public ResponseEntity<List<?>> getAllMerchants() {
        return ResponseEntity.ok().build();
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/merchants/{merchantId}")
    public ResponseEntity<List<?>> getMerchantById(@PathVariable("merchantId") UUID merchantId) {
        return ResponseEntity.ok().build();
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/products")
    public ResponseEntity<Page<?>> getProductsByFilters(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam(required = false) String merchantIds,
            @RequestParam(required = false) String productCategoryIds,
            @RequestParam(required = false) BigDecimal minOriginalPrice,
            @RequestParam(required = false) BigDecimal maxOriginalPrice,
            @RequestParam(required = false) BigDecimal minCashbackRate,
            @RequestParam(required = false) BigDecimal maxCashbackRate,
            @RequestParam(required = false) BigDecimal minDiscountRate,
            @RequestParam(required = false) BigDecimal maxDiscountRate,
            @RequestParam(required = false) String searchTerm
    ) {
        return ResponseEntity.ok().build();
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/products/{productId}")
    public ResponseEntity<List<?>> getProductById(@PathVariable("productId") UUID productId) {
        return ResponseEntity.ok().build();
    }

}