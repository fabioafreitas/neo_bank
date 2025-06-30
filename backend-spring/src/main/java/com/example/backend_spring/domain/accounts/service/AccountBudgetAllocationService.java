package com.example.backend_spring.domain.accounts.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountBudgetAllocationUpdateRequestDTO;
import com.example.backend_spring.domain.accounts.model.Account;
import com.example.backend_spring.domain.accounts.model.AccountBudgetAllocation;
import com.example.backend_spring.domain.accounts.model.BudgetCategory;
import com.example.backend_spring.domain.accounts.repository.AccountBudgetAllocationRepository;
import com.example.backend_spring.domain.accounts.repository.AccountRepository;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

@Service
public class AccountBudgetAllocationService {

    @Autowired
    private AccountBudgetAllocationRepository accountBudgetAllocationRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private BudgetCategoryService budgetCategoryService;

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    
    private List<AccountBudgetAllocation> getAllocationsByAccount(Account account) {
        return accountBudgetAllocationRepository.findByAccount(account)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account budget allocations not found"
            ));
    }

    private Account getAccountByUser(User user) {
        return accountRepository.findByUser(user)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    }

    private Account getAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    }

    public List<AccountBudgetAllocation> findAccountBudgetAllocationsByJwt() {
        User user = jwtTokenProviderService.getContextUser();
        Account account = getAccountByUser(user);
        return getAllocationsByAccount(account);
    }

    public List<AccountBudgetAllocationDTO> findAccountBudgetAllocationsDtoByJwt() {
        return findAccountBudgetAllocationsByJwt().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public List<AccountBudgetAllocationDTO> findByAccountNumber(String accountNumber) {
        Account account = getAccountByAccountNumber(accountNumber);
        return getAllocationsByAccount(account).stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }

    public void create(Account account) {
        List<BudgetCategory> budgetCategories = budgetCategoryService.findAllBudgetCategories();
        List<AccountBudgetAllocation> accountAllocations = budgetCategories.stream()
            .map(category -> new AccountBudgetAllocation(account, category, BigDecimal.ZERO))
            .collect(Collectors.toList());
        accountBudgetAllocationRepository.saveAll(accountAllocations);
    }

    public List<AccountBudgetAllocationDTO> update(AccountBudgetAllocationUpdateRequestDTO dto) {
        // check if there isn't repeated IDs in the request
        List<AccountBudgetAllocationDTO> updateAllocations = dto.allocations();
        List<UUID> updateAllocationIds = dto.allocations().stream()
            .map(AccountBudgetAllocationDTO::budgetCategoryId)
            .collect(Collectors.toList());
        if (updateAllocationIds.size() != updateAllocationIds.stream().distinct().count()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Duplicate budget category IDs found in the request");
        }
        
        List<AccountBudgetAllocation> allocations = this.findAccountBudgetAllocationsByJwt();
        
        // check if all IDs in the request exist in the database
        List<UUID> existingIds = allocations.stream()
            .map(AccountBudgetAllocation::getId)
            .collect(Collectors.toList());
        for (UUID updateAllocationId : updateAllocationIds) {
            if (!existingIds.contains(updateAllocationId)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget category with ID " + updateAllocationId + " not found");
            }
        }

        for (AccountBudgetAllocation allocation : allocations) {
            Optional<AccountBudgetAllocationDTO> updateDto = updateAllocations.stream()
                .filter(updateAllocation -> updateAllocation.budgetCategoryId().equals(allocation.getId()))
                .findFirst();
            if (updateDto.isPresent()) {
                allocation.setAllocationValue(updateDto.get().allocationValue());
            }
        }
        
        return accountBudgetAllocationRepository.saveAll(allocations)
            .stream()
            .map(this::toDto)
            .collect(Collectors.toList());
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