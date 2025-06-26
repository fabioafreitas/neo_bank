package com.example.backend_spring.domain.accounts.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.BudgetAllocationRequestDTO;
import com.example.backend_spring.domain.accounts.dto.BudgetAllocationResponseDTO;
import com.example.backend_spring.domain.accounts.dto.BudgetAllocationDeletedDTO;
import com.example.backend_spring.domain.accounts.service.AccountBudgetService;

@RestController
@RequestMapping("/api/accounts")
public class AccountBudgetController {

    @Autowired
    private AccountBudgetService accountBudgetService;

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/budget")
    public ResponseEntity<List<BudgetAllocationResponseDTO>> getCurrentUserBudgetAllocations() {
        return ResponseEntity.ok(accountBudgetService.findByJwt());
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/me/budget")
    public ResponseEntity<BudgetAllocationResponseDTO> createBudgetAllocation(@RequestBody BudgetAllocationRequestDTO dto) {
        return ResponseEntity.ok(accountBudgetService.createBudgetAllocation(dto));
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/me/budget/{categoryId}")
    public ResponseEntity<BudgetAllocationResponseDTO> updateBudgetAllocation(
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody BudgetAllocationRequestDTO dto) {
        return ResponseEntity.ok(accountBudgetService.updateBudgetAllocation(categoryId, dto));
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/me/budget/{categoryId}")
    public ResponseEntity<BudgetAllocationDeletedDTO> removeBudgetAllocation(@PathVariable("categoryId") UUID categoryId) {
        accountBudgetService.removeBudgetAllocation(categoryId);
        return ResponseEntity.ok(new BudgetAllocationDeletedDTO("Budget allocation removed successfully"));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountNumber}/budget")
    public ResponseEntity<List<BudgetAllocationResponseDTO>> getBudgetAllocationsByAccountNumber(
            @PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountBudgetService.findByAccountNumber(accountNumber));
    }
}