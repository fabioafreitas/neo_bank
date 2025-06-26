package com.example.backend_spring.domain.accounts.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.backend_spring.domain.accounts.dto.BudgetAllocationRequestDTO;
import com.example.backend_spring.domain.accounts.dto.BudgetAllocationResponseDTO;

@Service
public class AccountBudgetService {

    public List<BudgetAllocationResponseDTO> findByJwt() {
        // TODO: implement method
        return null;
    }

    public BudgetAllocationResponseDTO createBudgetAllocation(BudgetAllocationRequestDTO dto) {
        // TODO: implement method
        return null;
    }

    public BudgetAllocationResponseDTO updateBudgetAllocation(UUID categoryId, BudgetAllocationRequestDTO dto) {
        // TODO: implement method
        return null;
    }

    public void removeBudgetAllocation(UUID categoryId) {
        // TODO: implement method
    }

    public List<BudgetAllocationResponseDTO> findByAccountNumber(String accountNumber) {
        // TODO: implement method
        return null;
    }
}