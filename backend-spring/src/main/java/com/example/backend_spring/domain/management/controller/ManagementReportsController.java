package com.example.backend_spring.domain.management.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.management.service.ManagementReportsService;

@RestController
@RequestMapping("/api/management/reports")
public class ManagementReportsController {

    @Autowired
    private ManagementReportsService managementReportsService;

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/budget-allocation-usage")
    public ResponseEntity<?> getBudgetAllocationUsageReport() {
        return ResponseEntity.ok(managementReportsService.getBudgetAllocationUsageReport());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/cashback-summary")
    public ResponseEntity<?> getCashbackSummaryReport() {
        return ResponseEntity.ok(managementReportsService.getCashbackSummaryReport());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/merchant-performance")
    public ResponseEntity<?> getMerchantPerformanceReport() {
        return ResponseEntity.ok(managementReportsService.getMerchantPerformanceReport());
    }
}
