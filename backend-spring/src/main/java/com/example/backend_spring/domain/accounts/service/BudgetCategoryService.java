package com.example.backend_spring.domain.accounts.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.accounts.model.BudgetCategory;
import com.example.backend_spring.domain.accounts.repository.BudgetCategoryRepository;

@Service
public class BudgetCategoryService {

    private final BudgetCategoryRepository budgetCategoryRepository;

    public BudgetCategoryService(BudgetCategoryRepository budgetCategoryRepository) {
        this.budgetCategoryRepository = budgetCategoryRepository;
    }

    public List<BudgetCategory> findAllBudgetCategories() {
        return budgetCategoryRepository.findAll();
    }

    public List<BudgetCategoryDTO> getAllBudgetCategories() {
        return findAllBudgetCategories().stream()
            .map(this::toDto)
            .collect(Collectors.toList());
    }
    
    private BudgetCategoryDTO toDto(BudgetCategory budgetCategory) {
        if (budgetCategory == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Budget category not found");
        }
        return new BudgetCategoryDTO(
            budgetCategory.getName(),
            budgetCategory.getId()
        );
    }

    /*
     * I'm not implementing the CRUD methods for BudgetCategoryService
     * to simplify for now my project. I know it's not a good practice,
     * however, I'm inserting manually in DB these categories to gain time
     */

}