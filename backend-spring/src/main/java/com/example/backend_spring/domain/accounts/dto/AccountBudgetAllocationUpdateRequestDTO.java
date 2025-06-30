package com.example.backend_spring.domain.accounts.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AccountBudgetAllocationUpdateRequestDTO(
    List<AccountBudgetAllocationDTO> allocations
) {}