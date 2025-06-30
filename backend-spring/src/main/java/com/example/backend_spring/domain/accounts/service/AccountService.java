package com.example.backend_spring.domain.accounts.service;

import com.example.backend_spring.security.encoder.PepperPasswordEncoder;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.dto.AccountCreationDTO;
import com.example.backend_spring.domain.accounts.dto.AccountResponseDTO;
import com.example.backend_spring.domain.accounts.dto.AccountUpdateDTO;
import com.example.backend_spring.domain.accounts.model.Account;
import com.example.backend_spring.domain.accounts.repository.AccountRepository;
import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.domain.users.repository.UserRepository;
import com.example.backend_spring.domain.users.utils.UserRole;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AccountService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PepperPasswordEncoder pepperPasswordEncoder;

    @Autowired
    private AccountBudgetAllocationService accountBudgetAllocationService;

    public List<AccountResponseDTO> findAll() {
        return accountRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    public Page<AccountResponseDTO> findAll(int page, int size, String sort, String status, BigDecimal minValue, BigDecimal maxValue) {
         // Split the sort parameter into field and direction
        String[] sortParts = sort.split(",");
        String sortField = sortParts[0];
        Sort.Direction sortDirection = sortParts.length > 1 && sortParts[1].equalsIgnoreCase("desc") 
            ? Sort.Direction.DESC 
            : Sort.Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortField));

        Specification<Account> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), AccountStatus.valueOf(status)));
            }
            if (minValue != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("balance"), minValue));
            }
            if (maxValue != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("balance"), maxValue));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        return accountRepository.findAll(specification, pageable).map(this::toDto);
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
        User user = jwtTokenProviderService.getContextUser();
        if (user.getRole() != UserRole.CLIENT) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized access");
        }
        return accountRepository.findByUser(user).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Account not found"
            ));
    } 

    public AccountResponseDTO create(AccountCreationDTO dto) {
        String encryptedTransactionPassword = pepperPasswordEncoder.encode(dto.transactionPassword());

        Account account = new Account(
            dto.user(),
            dto.balance(),
            dto.status(),
            generateUniqueAccountNumber(),
            encryptedTransactionPassword
        );

        // save account in DB
        Account savedAccount = accountRepository.save(account);
        
        // create account budget allocations of new account
        accountBudgetAllocationService.create(account);

        return toDto(savedAccount);
    }

    public AccountResponseDTO update(Account account) {
        AccountUpdateDTO dto = new AccountUpdateDTO(
            account.getBalance(),
            account.getStatus(),
            account.getTransactionPassword()
        );
        return this.update(account.getAccountNumber(), dto);
    }

    public AccountResponseDTO update(String accountNumber, AccountUpdateDTO dto) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(dto.balance() != null) {
            account.setBalance(dto.balance());
        }
        if(dto.status() != null) {
            account.setStatus(dto.status());
        }
        if(dto.transactionPassword() != null) {
            account.setTransactionPassword(dto.transactionPassword());
        }
        
        return toDto(accountRepository.save(account));
    }

    public void activateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));
        if(account.getStatus() == AccountStatus.DEACTIVATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is deactivated. Status cant be changed anymore.");
        }
        account.setStatus(AccountStatus.ACTIVE); 
        accountRepository.save(account);
    }

    public void suspendAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getStatus() == AccountStatus.DEACTIVATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is deactivated. Status cant be changed anymore.");
        }
        account.setStatus(AccountStatus.SUSPENDED); 
        accountRepository.save(account);
    }

    public void deactivateAccount(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found"));

        if(account.getStatus() == AccountStatus.DEACTIVATED) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account is deactivated. Status cant be changed anymore.");
        }
        
        if(account.getBalance().compareTo(new BigDecimal(0.0)) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't delete account with a positive balance");
        }

        // Get user reference
        User user = account.getUser();
        
        // Define account as deactivated
        account.setStatus(AccountStatus.DEACTIVATED); 
        account.setUser(null);
        accountRepository.save(account);

        // Delete the user associated with the account
        // Explicitly delete the user
        if(user == null) {
            userRepository.delete(user);
        }
        
    }

    private String generateUniqueAccountNumber() {
        String accountNumber;
        do {
            long number = (long) (Math.random() * 9_000_000_000L) + 1_000_000_000L;
            accountNumber = String.valueOf(number);
        } while (accountRepository.existsByAccountNumber(accountNumber));
        return accountNumber;
    }

    
    
    private AccountResponseDTO toDto(Account account) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return new AccountResponseDTO(
            account.getUser().getUserProfile().getFirstName(),
            account.getUser().getUserProfile().getLastName(),
            account.getUser().getUserProfile().getEmail(),
            account.getBalance(),
            account.getStatus(),
            account.getAccountNumber()
        );
    }

    public boolean isValidTransactionPassword(String transactionPassword) {
        return transactionPassword != null && transactionPassword.matches("\\d{6}");
    }

    public boolean isValidAccessPassword(String accessPassword) {
        return accessPassword != null && accessPassword.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^a-zA-Z\\d]).{8,}$");
    }
}
