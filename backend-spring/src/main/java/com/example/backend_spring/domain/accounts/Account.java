package com.example.backend_spring.domain.accounts;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.backend_spring.domain.users.User;

@Table(name = "accounts")
@Entity(name = "accounts")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Account {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Setter
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Setter
    @Column(columnDefinition = "DECIMAL(15, 2)")
    private BigDecimal balance;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "account_status")
    private AccountStatus status;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "account_type")
    private AccountType type;

    public Account(User user, BigDecimal balance, AccountStatus status, AccountType type) {
        this.user = user;
        this.balance = balance;
        this.status = status;
        this.type = type;
    }
}