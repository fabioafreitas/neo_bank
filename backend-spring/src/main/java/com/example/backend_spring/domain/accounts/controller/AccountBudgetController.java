package com.example.backend_spring.domain.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationUpdateRequestDTO;
import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.accounts.service.AccountBudgetAllocationService;
import com.example.backend_spring.domain.accounts.service.BudgetCategoryService;

@RestController
@RequestMapping("/api/accounts")
public class AccountBudgetController {

    private final AccountBudgetAllocationService accountBudgetAllocationService;

    private final BudgetCategoryService budgetCategoryService;

    public AccountBudgetController(AccountBudgetAllocationService accountBudgetAllocationService, BudgetCategoryService budgetCategoryService) {
        this.accountBudgetAllocationService = accountBudgetAllocationService;
        this.budgetCategoryService = budgetCategoryService;
    }


    @GetMapping("/budgetCategories")
    public ResponseEntity<List<BudgetCategoryDTO>> getBudgetCategories() {
        return ResponseEntity.ok(budgetCategoryService.getAllBudgetCategories());
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me/budget")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> getCurrentUserBudgetAllocations() {
        return ResponseEntity.ok(accountBudgetAllocationService.findAccountBudgetAllocationsDtoByJwt());
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/me/budget")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> updateBudgetAllocation(
            @RequestBody AccountBudgetAllocationUpdateRequestDTO dto) {
        return ResponseEntity.ok(accountBudgetAllocationService.update(dto));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountNumber}/budget")
    public ResponseEntity<List<AccountBudgetAllocationDTO>> getBudgetAllocationsByAccountNumber(
            @PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountBudgetAllocationService.findByAccountNumber(accountNumber));
    }
}