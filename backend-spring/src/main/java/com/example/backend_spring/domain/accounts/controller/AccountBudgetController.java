package com.example.backend_spring.domain.accounts.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountGeneralResponseDTO;
import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.accounts.service.AccountBudgetAllocationService;
import com.example.backend_spring.domain.accounts.service.BudgetCategoryService;

@RestController
@RequestMapping("/api/accounts")
public class AccountBudgetController {

    @Autowired
    private AccountBudgetAllocationService accountBudgetAllocationService;

    @Autowired
    private BudgetCategoryService budgetCategoryService;


    @GetMapping("/budgetCategories")
    public ResponseEntity<List<BudgetCategoryDTO>> getBudgetCategories() {
        return ResponseEntity.ok(budgetCategoryService.getAllBudgetCategories());
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/budget")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> getCurrentUserBudgetAllocations() {
        return ResponseEntity.ok(accountBudgetAllocationService.findByJwt());
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/me/budget/{categoryId}")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> updateBudgetAllocation(
            @PathVariable("categoryId") UUID categoryId,
            @RequestBody List<AccountBudgetAllocationDTO> dto) {
        return ResponseEntity.ok(accountBudgetAllocationService.updateBudgetAllocation(categoryId, dto));
    }

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/me/budget/{categoryId}")
    public ResponseEntity<AccountGeneralResponseDTO> removeBudgetAllocation(@PathVariable("categoryId") UUID categoryId) {
        accountBudgetAllocationService.removeBudgetAllocation(categoryId);
        return ResponseEntity.ok(new AccountGeneralResponseDTO("Budget allocation removed successfully"));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountNumber}/budget")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> getBudgetAllocationsByAccountNumber(
            @PathVariable("accountNumber") String accountNumber) {
        return null;//ResponseEntity.ok(accountBudgetAllocationService.findByAccountNumber(accountNumber));
    }
}