package com.example.backend_spring.domain.accounts.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountDeactivatedDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    // TODO review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getByJwt() {
        return ResponseEntity.ok(accountService.findByJwt());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountService.findAccountDtoByAccountNumber(accountNumber));
    }

    // TODO review
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<AccountDeactivatedDTO> deactivateAccount(@PathVariable("accountNumber") String accountNumber) {
        accountService.deactivateAccount(accountNumber);
        return ResponseEntity.ok(new AccountDeactivatedDTO("Account deleted successfully"));
    }
}
