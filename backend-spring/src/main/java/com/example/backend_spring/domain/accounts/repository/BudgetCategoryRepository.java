package com.example.backend_spring.domain.accounts.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.backend_spring.domain.accounts.model.BudgetCategory;

public interface BudgetCategoryRepository extends JpaRepository<BudgetCategory, UUID> {
}
