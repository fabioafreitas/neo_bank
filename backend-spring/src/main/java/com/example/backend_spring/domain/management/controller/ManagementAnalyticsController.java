package com.example.backend_spring.domain.management.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.management.service.ManagementAnalyticsService;

@RestController
@RequestMapping("/api/management/analytics")
public class ManagementAnalyticsController {

    @Autowired
    private ManagementAnalyticsService managementAnalyticsService;

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/budget/categories")
    public ResponseEntity<List<?>> getBudgetCategoryTransactionSummary() {
        return ResponseEntity.ok(managementAnalyticsService.getBudgetCategoryTransactionSummary());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountNumber}/budget")
    public ResponseEntity<List<?>> getAccountBudgetOverview(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(managementAnalyticsService.getAccountBudgetOverview(accountNumber));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/accounts/{accountNumber}/budget/summary")
    public ResponseEntity<List<?>> getAccountBudgetCategorySummary(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(managementAnalyticsService.getAccountBudgetCategorySummary(accountNumber));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions/pending")
    public ResponseEntity<List<?>> getPendingTransactionRequests() {
        return ResponseEntity.ok(managementAnalyticsService.getPendingTransactionRequests());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/transactions/summary")
    public ResponseEntity<?> getTransactionSummaryStatistics() {
        return ResponseEntity.ok(managementAnalyticsService.getTransactionSummaryStatistics());
    }
}