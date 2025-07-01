package com.example.backend_spring.domain.transactions.model;

import com.example.backend_spring.domain.transactions.utils.TransactionStatus;
import com.example.backend_spring.domain.users.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "transaction_requests")
@Entity(name = "transaction_requests")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TransactionRequest {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requested_by", nullable = false)
    private User requestedBy;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewed_by")
    private User reviewedBy;

    @Setter
    @Column(name = "reviewed_at")
    private OffsetDateTime reviewedAt;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "TEXT", nullable = false)
    private TransactionStatus status;

    public TransactionRequest(
            Transaction transaction,
            User requestedBy
        ) {
        this.transaction = transaction;
        this.requestedBy = requestedBy;
        this.status = TransactionStatus.PENDING;
    }
}