package com.example.backend_spring.domain.transactions.controller;

import java.util.List;
import java.util.UUID;

import com.example.backend_spring.domain.transactions.dto.TransactionTransferRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionTransferResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.transactions.dto.TransactionRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionResponseDTO;
import com.example.backend_spring.domain.transactions.service.TransactionService;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/{transactionNumber}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable("transactionNumber") UUID transactionNumber) {
        return ResponseEntity.ok(transactionService.findByTransactionNumber(transactionNumber));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/operations/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@RequestBody @Valid TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.withdraw(dto));
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/operations/depositRequest")
    public ResponseEntity<TransactionResponseDTO> depositRequest(@RequestBody @Valid TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.depositRequest(dto));
    }

    // TODO: review
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/operations/purchase")
    public ResponseEntity<TransactionResponseDTO> purchase(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/operations/transfer")
    public ResponseEntity<TransactionTransferResponseDTO> transfer(@RequestBody @Valid TransactionTransferRequestDTO dto) {
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
