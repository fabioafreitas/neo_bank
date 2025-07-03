package com.example.backend_spring.domain.merchants.model;

import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.users.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Table(name = "merchants")
@Entity(name = "merchants")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Merchant {
	@Id
	@GeneratedValue
	@Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
	private UUID id;

	@OneToOne
	@JoinColumn(name = "user_id")
	private User user;

	@Setter
	@Column(name = "store_name", nullable = false, unique = true)
	private String storeName;

	@Setter
	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "created_at", nullable = false)
	private OffsetDateTime createdAt;

	public Merchant(User user, String storeName, String description) {
		this.user = user;
		this.storeName = storeName;
		this.description = description;
		this.createdAt = OffsetDateTime.now();
	}
}
