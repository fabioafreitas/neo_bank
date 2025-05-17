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
import com.example.backend_spring.domain.users.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;
    

    AccountService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<AccountResponseDTO> findAll() {
        return accountRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public AccountResponseDTO findById(UUID id) {
        return accountRepository.findById(id).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    }

    public Account findAccountByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }
    
    public AccountResponseDTO findAccountDtoByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
    }

    public Account findAccountByUser(User user) {
        return accountRepository.findByUser(user)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
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

    public AccountResponseDTO update(Account account) {
        AccountUpdateDTO dto = new AccountUpdateDTO(
            account.getBalance(),
            account.getType(),
            account.getStatus()
        );
        return this.update(account.getAccountNumber(), dto);
    }

    public AccountResponseDTO update(String accountNumber, AccountUpdateDTO dto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
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

    public void deactivateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getBalance().compareTo(new BigDecimal(0.0)) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete account with a positive balance");
        }

        // Get user reference
        User user = account.getUser();

        // Define account as deactivated
        account.setStatus(AccountStatus.DEACTIVATED); 
        account.setUser(null);
        accountRepository.save(account);

        // Delete the user associated with the account
        userRepository.delete(user);
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
        UUID userId = null;
        if (account.getUser() != null) {
            userId = account.getUser().getId();
        }
        return new AccountResponseDTO(
            userId,
            account.getId(),
            account.getBalance(),
            account.getType(),
            account.getStatus(),
            account.getAccountNumber()
        );
    }
}
