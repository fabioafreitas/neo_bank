package com.example.backend_spring.domain.transactions.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.transactions.dto.TransactionPurchaseCashbackDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionResponseDTO;
import com.example.backend_spring.domain.transactions.service.TransactionService;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{transactionNumber}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable("transactionNumber") UUID transactionNumber) {
        return ResponseEntity.ok(transactionService.findByTransactionNumber(transactionNumber));
    }
    
    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.withdraw(dto));
    }

    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/depositRequest")
    public ResponseEntity<TransactionResponseDTO> depositRequest(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.deposit(dto));
    }

    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/purchase")
    public ResponseEntity<TransactionPurchaseCashbackDTO> purchase(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.purchase(dto));
    }

    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.transfer(dto));
    }
    
    // TODO: Implement pagination and sorting for the getAll method
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getTransactionsByFilters() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    // TODO: implement method, calling getTransactionsByFilters service method
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<List<TransactionResponseDTO>> getMeTransactionsByFilters() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    // TODO: review
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/approve/{transactionNumber}")
    public ResponseEntity<TransactionResponseDTO> approveRequestTransaction(@PathVariable("transactionNumber") UUID transactionNumber) {
        return ResponseEntity.ok().build();
    }

    // TODO: review
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reject/{transactionNumber}")
    public ResponseEntity<TransactionResponseDTO> rejectRequestTransaction(@PathVariable("transactionNumber") UUID transactionNumber) {
        return ResponseEntity.ok().build();
    }
}
