package com.example.backend_spring.domain.transactions.repository;

import com.example.backend_spring.domain.transactions.model.TransferTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransferTransactionRepository extends JpaRepository<TransferTransaction, UUID> {
}
