package com.example.backend_spring.domain.accounts;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.dto.AccountUpdateDTO;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping
    public ResponseEntity<List<AccountResponseDTO>> getAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(accountService.findById(id));
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getByJwt() {
        return ResponseEntity.ok(accountService.findByJwt());
    }

    // Creation of new accounts is not allowed through this endpoint, since the user domain handles it

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable("id") UUID id, @RequestBody AccountUpdateDTO dto) {
        return ResponseEntity.ok(accountService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") UUID id) {

        accountService.deactivateAccount(id);
        return ResponseEntity.ok("Bank account deleted successfully");
    }
}
