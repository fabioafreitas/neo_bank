package com.example.backend_spring.domain.accounts;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.accounts.dto.AccountDeactivatedDTO;
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

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> getByAccountNumber(@PathVariable("accountNumber") String accountNumber) {
        return ResponseEntity.ok(accountService.findAccountDtoByAccountNumber(accountNumber));
    }

    @GetMapping("/me")
    public ResponseEntity<AccountResponseDTO> getByJwt() {
        return ResponseEntity.ok(accountService.findByJwt());
    }

    // Creation of new accounts is not allowed through this endpoint, since the user domain handles it

    @PutMapping("/{accountNumber}")
    public ResponseEntity<AccountResponseDTO> update(@PathVariable("accountNumber") String accountNumber, @RequestBody AccountUpdateDTO dto) {
        return ResponseEntity.ok(accountService.update(accountNumber, dto));
    }

    @DeleteMapping("/{accountNumber}")
    public ResponseEntity<AccountDeactivatedDTO> delete(@PathVariable("accountNumber") String accountNumber) {
        accountService.deactivateAccount(accountNumber);
        return ResponseEntity.ok(new AccountDeactivatedDTO("Account deleted successfully"));
    }
}
