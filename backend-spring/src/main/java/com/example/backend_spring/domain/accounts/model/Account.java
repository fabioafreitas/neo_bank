package com.example.backend_spring.domain.accounts.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.users.model.User;

@Table(name = "accounts")
@Entity(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    @Column(name = "balance", columnDefinition = "DECIMAL(15, 2)")
    private BigDecimal balance;

    @Column(name = "account_number", nullable = false, unique = true)
    private String accountNumber;

    @Setter
    @Column(name = "transaction_password", nullable = false)
    private String transactionPassword;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "TEXT")
    private AccountStatus status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Setter
    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    public Account(User user, BigDecimal balance, AccountStatus status, String accountNumber, String transactionPassword) {
        this.user = user;
        this.balance = balance;
        this.status = status;
        this.accountNumber = accountNumber;
        this.transactionPassword = transactionPassword;
        OffsetDateTime now = OffsetDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}