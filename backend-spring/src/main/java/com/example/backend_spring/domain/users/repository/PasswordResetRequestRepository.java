package com.example.backend_spring.domain.users.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.users.model.PasswordResetRequest;
import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;

import jakarta.transaction.Transactional;

public interface PasswordResetRequestRepository extends JpaRepository<PasswordResetRequest, UUID> {
    Optional<PasswordResetRequest> findById(UUID id);
    
    Optional<PasswordResetRequest> findByToken(UUID token);

    @Transactional
    void deleteByUserIdAndType(UUID userId, PasswordResetRequestType type);
}