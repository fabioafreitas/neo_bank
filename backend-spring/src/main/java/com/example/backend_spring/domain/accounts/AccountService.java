package com.example.backend_spring.domain.accounts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.AccountCreationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.dto.AccountUpdateDTO;
import com.example.backend_spring.domain.users.User;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public List<AccountResponseDTO> findAll() {
        return accountRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccountResponseDTO findById(UUID id) {
        return accountRepository.findById(id).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    }

    public AccountResponseDTO findByJwt() {
        User user = this.getContextUser();
        return accountRepository.findByUser(user).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    } 

    public AccountResponseDTO create(AccountCreationDTO dto) {
        Account account = new Account(
            dto.user(),
            dto.balance(),
            dto.status(),
            dto.type(),
            generateUniqueAccountNumber()
        );
        return toDto(accountRepository.save(account));
    }

    // Not account transfer allowed in the simple project
    public AccountResponseDTO update(UUID id, AccountUpdateDTO dto) {
        Account account = accountRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        
            if(dto.type() != null) {
            account.setType(dto.type());
        }
        if(dto.balance() != null) {
            account.setBalance(dto.balance());
        }
        if(dto.status() != null) {
            account.setStatus(dto.status());
        }
        
        return toDto(accountRepository.save(account));
    }

    public void deactivateAccount(UUID accountId) {
        Account account = accountRepository.findById(accountId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getBalance().compareTo(new BigDecimal(0.0)) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't deactivate account with a positive balance");
        }

        account.setStatus(AccountStatus.DEACTIVATED); 
        accountRepository.save(account);
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            long number = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
            accountNumber = String.valueOf(number);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    private User getContextUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    
    private AccountResponseDTO toDto(Account account) {
        return new AccountResponseDTO(
            account.getUser().getId(),
            account.getId(),
            account.getBalance(),
            account.getType(),
            account.getStatus(),
            account.getAccountNumber()
        );
    }
}
