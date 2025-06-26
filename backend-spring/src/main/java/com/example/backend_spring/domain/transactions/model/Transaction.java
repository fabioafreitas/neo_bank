package com.example.backend_spring.domain.transactions.model;

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
import com.example.backend_spring.domain.transactions.utils.TransactionType;


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

    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private Account account;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "TEXT")
    private TransactionType type;

    @Column(name = "description")
    private String description;
    
    @Setter
    @Column(name = "failure_message")
    private String failureMessage;
    
    @Setter
    @Column(name = "is_success")
    private boolean isSuccess;

    @Column(name = "amount", columnDefinition = "DECIMAL(15, 2)")
    private BigDecimal amount;

    // created inside DB now()
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    public Transaction(
            UUID transactionNumber, 
            Account account, 
            TransactionType type, 
            String description, 
            BigDecimal amount, 
            boolean isSuccess
        ) {
        this.transactionNumber = transactionNumber;
        this.account = account;
        this.type = type;
        this.description = description;
        this.isSuccess = isSuccess;
        this.amount = amount;
        this.createdAt = OffsetDateTime.now();
    }
}