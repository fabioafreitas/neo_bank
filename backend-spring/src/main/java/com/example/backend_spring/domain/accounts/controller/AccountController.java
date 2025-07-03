package com.example.backend_spring.domain.accounts.controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountGeneralResponseDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getByJwt() {
        return ResponseEntity.ok(accountService.findByJwt());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<AccountResponseDTO>> getAll(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam(required = false) OffsetDateTime createdAtStartDate,
            @RequestParam(required = false) OffsetDateTime createdAtEndDate,
            @RequestParam(required = false) OffsetDateTime updatedAtStartDate,
            @RequestParam(required = false) OffsetDateTime updatedAtEndDate,
            @RequestParam(required = false) String accountStatus,
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam(required = false) BigDecimal maxValue) {
        return ResponseEntity.ok(accountService.findByFilters(
                page,
                size,
                sort,
                createdAtStartDate,
                createdAtEndDate,
                updatedAtStartDate,
                updatedAtEndDate,
                accountStatus,
                minValue,
                maxValue
        ));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountService.findAccountDtoByAccountNumber(accountNumber));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/activate/{accountNumber}")
    public ResponseEntity<AccountGeneralResponseDTO> activateAccount(@PathVariable("accountNumber") String accountNumber) {
        accountService.activateAccount(accountNumber);
        return ResponseEntity.ok(new AccountGeneralResponseDTO("Account activated successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/suspend/{accountNumber}")
    public ResponseEntity<AccountGeneralResponseDTO> suspendAccount(@PathVariable("accountNumber") String accountNumber) {
        accountService.suspendAccount(accountNumber);
        return ResponseEntity.ok(new AccountGeneralResponseDTO("Account suspended successfully"));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/deactivate/{accountNumber}")
    public ResponseEntity<AccountGeneralResponseDTO> deactivateAccount(@PathVariable("accountNumber") String accountNumber) {
        accountService.deactivateAccount(accountNumber);
        return ResponseEntity.ok(new AccountGeneralResponseDTO("Account deactivated successfully. Related user deleted."));
    }
}
