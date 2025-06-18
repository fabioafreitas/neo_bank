package com.example.backend_spring.domain.users.model;

import java.time.OffsetDateTime;
import java.util.UUID;

import com.example.backend_spring.domain.users.utils.PasswordResetRequestType;
import com.example.backend_spring.domain.users.utils.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "password_reset_requests")
@Table(name = "password_reset_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class PasswordResetRequest {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token", nullable = false)
    private UUID token;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "TEXT")
    private PasswordResetRequestType type;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    // This field is automatically set by the database, so we don't 
    // need to set it manually. Therefore intertable = false
    @Column(name = "expires_at", insertable = false, updatable = false)
    private OffsetDateTime expiresAt;

    public PasswordResetRequest(User user, PasswordResetRequestType type) {
        this.token = UUID.randomUUID();
        this.user = user;
        this.type = type;
        this.createdAt = OffsetDateTime.now();
    }
}