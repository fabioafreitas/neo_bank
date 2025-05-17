package com.example.backend_spring.domain.transactions;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.accounts.Account;

public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    Optional<Transaction> findByTransactionNumber(UUID transactionNumber);
    List<Transaction> findByAccount(Account account);
    Optional<Transaction> findByTransactionNumberAndAccount(UUID transactionNumber, Account account);
}
