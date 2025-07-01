package com.example.backend_spring.domain.transactions.model;

import com.example.backend_spring.domain.accounts.model.BudgetCategory;
import com.example.backend_spring.domain.transactions.utils.TransactionOperationType;
import com.example.backend_spring.domain.transactions.utils.TransactionStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.accounts.model.Account;


@Table(name = "transactions")
@Entity(name = "transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Transaction {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID transactionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_category_id")
    private BudgetCategory budgetCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_type", columnDefinition = "TEXT", nullable = false)
    private TransactionOperationType operationType;

    @Column(name = "description", nullable = false)
    private String description;

    @Setter
    @Column(name = "rejection_message")
    private String rejectionMessage;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "TEXT", nullable = false)
    private TransactionStatus status;

    @Column(name = "amount", columnDefinition = "DECIMAL(15, 2)")
    private BigDecimal amount;

    // created inside DB now()
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public Transaction(
            UUID transactionNumber,
            Account account,
            BudgetCategory budgetCategory,
            TransactionOperationType operationType,
            String description,
            BigDecimal amount,
            TransactionStatus status
    ) {
        this.transactionNumber = transactionNumber;
        this.account = account;
        this.budgetCategory = budgetCategory;
        this.operationType = operationType;
        this.description = description;
        this.status = status;
        this.amount = amount;
        this.createdAt = OffsetDateTime.now();
    }

    public Transaction(
            UUID transactionNumber,
            Account account,
            TransactionOperationType operationType,
            String description,
            BigDecimal amount,
            TransactionStatus status
    ) {
        this.transactionNumber = transactionNumber;
        this.account = account;
        this.operationType = operationType;
        this.description = description;
        this.status = status;
        this.amount = amount;
        this.createdAt = OffsetDateTime.now();
    }
}