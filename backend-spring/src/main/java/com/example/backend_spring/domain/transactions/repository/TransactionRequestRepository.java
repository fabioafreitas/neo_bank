package com.example.backend_spring.domain.transactions.repository;

import com.example.backend_spring.domain.transactions.model.Transaction;
import com.example.backend_spring.domain.transactions.model.TransactionRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TransactionRequestRepository extends JpaRepository<TransactionRequest, UUID> {
}
