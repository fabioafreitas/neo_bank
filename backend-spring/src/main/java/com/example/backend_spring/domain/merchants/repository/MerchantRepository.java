package com.example.backend_spring.domain.merchants.repository;

import com.example.backend_spring.domain.merchants.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface MerchantRepository extends JpaRepository<Merchant, UUID>, JpaSpecificationExecutor<Merchant> {
	Optional<Merchant> findByUserId(UUID userId);
}
