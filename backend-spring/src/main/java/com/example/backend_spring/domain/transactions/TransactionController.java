package com.example.backend_spring.domain.transactions;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.backend_spring.domain.transactions.dto.TransactionPurchaseCashbackDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionResponseDTO;


@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @GetMapping
    public ResponseEntity<List<TransactionResponseDTO>> getAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDTO> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(transactionService.findByTransactionNumber(id));
    }
    
    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponseDTO> withdraw(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.withdraw(dto));
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponseDTO> deposit(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.deposit(dto));
    }

    @PostMapping("/purchase")
    public ResponseEntity<TransactionPurchaseCashbackDTO> purchase(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.purchase(dto));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponseDTO> transfer(@RequestBody TransactionRequestDTO dto) {
        return ResponseEntity.ok(transactionService.transfer(dto));
    }
    
}
