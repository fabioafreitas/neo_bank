package com.example.backend_spring.domain.transactions.controller;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.transactions.dto.TransactionTransferRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionTransferResponseDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<TransactionResponseDTO>> getTransactionsByFilters(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam(required = false) String accountNumbers,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String transactionStatus,
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam(required = false) BigDecimal maxValue
    ) {
        return ResponseEntity.ok(transactionService.findByFilters(
                page,
                size,
                sort,
                accountNumbers,
                startDate,
                endDate,
                operationType,
                transactionStatus,
                minValue,
                maxValue
        ));
    }

    // TODO: implement method, calling getTransactionsByFilters service method
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/me")
    public ResponseEntity<Page<TransactionResponseDTO>> getMeTransactionsByFilters(
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam String sort,
            @RequestParam(required = false) OffsetDateTime startDate,
            @RequestParam(required = false) OffsetDateTime endDate,
            @RequestParam(required = false) String operationType,
            @RequestParam(required = false) String transactionStatus,
            @RequestParam(required = false) BigDecimal minValue,
            @RequestParam(required = false) BigDecimal maxValue
    ) {
        return ResponseEntity.ok(transactionService.findByFiltersAndContextUser(
                page,
                size,
                sort,
                startDate,
                endDate,
                operationType,
                transactionStatus,
                minValue,
                maxValue
        ));
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
