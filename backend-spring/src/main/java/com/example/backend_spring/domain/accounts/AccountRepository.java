package com.example.backend_spring.domain.accounts;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.users.User;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    Optional<Account> findByUser(User user);
}
