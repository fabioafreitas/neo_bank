package com.example.backend_spring.domain.transactions.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.accounts.model.Account;
import com.example.backend_spring.domain.transactions.model.Transaction;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {
    Optional<Transaction> findByTransactionNumber(UUID transactionNumber);
    List<Transaction> findByAccount(Account account);
    Optional<Transaction> findByTransactionNumberAndAccount(UUID transactionNumber, Account account);
}
