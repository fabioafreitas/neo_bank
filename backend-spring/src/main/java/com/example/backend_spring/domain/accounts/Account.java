package com.example.backend_spring.domain.accounts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

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
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Setter
    @Column(name = "balance", columnDefinition = "DECIMAL(15, 2)")
    private BigDecimal balance;

    @Column(name = "account_number")
    private String accountNumber;

    @Setter
    @Column(name = "transaction_password")
    private String transactionPassword;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "TEXT")
    private AccountStatus status;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "TEXT")
    private AccountType type;

    public Account(User user, BigDecimal balance, AccountStatus status, AccountType type, String accountNumber, String transactionPassword) {
        this.user = user;
        this.balance = balance;
        this.status = status;
        this.type = type;
        this.accountNumber = accountNumber;
        this.transactionPassword = transactionPassword;
    }
}