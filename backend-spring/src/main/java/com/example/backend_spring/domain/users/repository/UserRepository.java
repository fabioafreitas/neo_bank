package com.example.backend_spring.domain.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.utils.UserRole;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    long countByRole(UserRole role);
}
