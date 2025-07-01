package com.example.backend_spring.domain.transactions.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Table(name = "transfer_transactions")
@Entity(name = "transfer_transactions")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TransferTransaction {
    @Id
    @GeneratedValue
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "source_transaction_id")
    private Transaction sourceTransaction;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_transaction_id")
    private Transaction destinationTransaction;

    public TransferTransaction(
            Transaction sourceTransaction,
            Transaction destinationTransaction
        ) {
        this.sourceTransaction = sourceTransaction;
        this.destinationTransaction = destinationTransaction;
    }
}