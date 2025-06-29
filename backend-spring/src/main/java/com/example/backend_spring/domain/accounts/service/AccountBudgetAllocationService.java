package com.example.backend_spring.domain.accounts.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationDTO;
import com.example.backend_spring.domain.accounts.model.AccountBudgetAllocation;
import com.example.backend_spring.domain.accounts.repository.AccountBudgetAllocationRepository;

@Service
public class AccountBudgetAllocationService {

    @Autowired
    private AccountBudgetAllocationRepository accountBudgetAllocationRepository;

    public List<AccountBudgetAllocationDTO> findByJwt() {
        // TODO: implement method
        return null;
    }

    /*
     * relacionar a tabela de buget_allocations, que armazena todas as alocações existentes no DB
     * com a tabela de accounts_budget_allocations, que armazena as alocações do usuário logado
     * 
     * 1. verifica se a categoria já existe
     * 2. se existir, atualiza a alocação
     */
    public List<AccountBudgetAllocationDTO> createBudgetAllocation(List<AccountBudgetAllocationDTO> dto) {
        // TODO: implement method
        return null;
    }

    public List<AccountBudgetAllocationDTO> updateBudgetAllocation(UUID categoryId, List<AccountBudgetAllocationDTO> dto) {
        // TODO: implement method
        return null;
    }

    public void removeBudgetAllocation(UUID categoryId) {
        // TODO: implement method
    }

    public AccountBudgetAllocationDTO findByAccountNumber(String accountNumber) {
        // TODO: implement method
        return null;
    }


    private AccountBudgetAllocationDTO toDto(AccountBudgetAllocation accountBudgetCategory) {
        if (accountBudgetCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account budget category not found");
        }
        return new AccountBudgetAllocationDTO(
            accountBudgetCategory.getBudgetCategory().getName(),
            accountBudgetCategory.getId(),
            accountBudgetCategory.getAllocationValue()
        );
    }
}