package com.example.backend_spring.domain.accounts.service;

import com.example.backend_spring.security.encoder.PepperPasswordEncoder;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

import com.example.backend_spring.utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class AccountService {

    private final UserRepository userRepository;

    private final JwtTokenProviderService jwtTokenProviderService;

    private final AccountRepository accountRepository;

    private final PepperPasswordEncoder pepperPasswordEncoder;

    private final AccountBudgetAllocationService accountBudgetAllocationService;

    private final List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
            "id",
            "balance",
            "accountNumber",
            "status",
            "createdAt",
            "updatedAt"
    );

    public AccountService(UserRepository userRepository, JwtTokenProviderService jwtTokenProviderService, AccountRepository accountRepository, PepperPasswordEncoder pepperPasswordEncoder, AccountBudgetAllocationService accountBudgetAllocationService) {
        this.userRepository = userRepository;
        this.jwtTokenProviderService = jwtTokenProviderService;
        this.accountRepository = accountRepository;
        this.pepperPasswordEncoder = pepperPasswordEncoder;
        this.accountBudgetAllocationService = accountBudgetAllocationService;
    }

    private void validateFindAllOptionalParams(
            OffsetDateTime createdAtStartDate,
            OffsetDateTime createdAtEndDate,
            OffsetDateTime updatedAtStartDate,
            OffsetDateTime updatedAtEndDate,
            String accountStatus,
            BigDecimal minValue,
            BigDecimal maxValue
    ) {
        if (createdAtStartDate != null &&
                createdAtEndDate != null &&
                createdAtStartDate.isAfter(createdAtEndDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "createdAtStartDate cannot be after createdAtEndDate");
        }
        if (updatedAtStartDate != null &&
                updatedAtEndDate != null &&
                updatedAtStartDate.isAfter(updatedAtEndDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "updatedAtStartDate cannot be after updatedAtEndDate");
        }
        if (accountStatus != null) {
            try {
                AccountStatus.valueOf(accountStatus);
            } catch (IllegalArgumentException e) {
                String acceptableValues = String.join(", ", Arrays.stream(AccountStatus.values())
                    .map(Enum::name)
                    .toList());
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "accountStatus invalid. Acceptable values are: " + acceptableValues);
            }
        }
        if (minValue != null && minValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minValue can't be negative");
        }
        if (maxValue != null && maxValue.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "maxValue can't be negative");
        }
        if (minValue != null && maxValue != null && minValue.compareTo(maxValue) > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minValue can't be larger than maxValue");
        }
    }

    public List<Account> findAllByAccountNumberIn(List<String> accountNumberList) {
        return accountRepository.findAllByAccountNumberIn(accountNumberList);
    }

    public Page<AccountResponseDTO> findByFilters(
            int page,
            int size,
            String sort,
            OffsetDateTime createdAtStartDate,
            OffsetDateTime createdAtEndDate,
            OffsetDateTime updatedAtStartDate,
            OffsetDateTime updatedAtEndDate,
            String accountStatus,
            BigDecimal minValue,
            BigDecimal maxValue) {
        PaginationUtils.validatePaginationParams(page, size, sort, ALLOWED_SORT_FIELDS);
        validateFindAllOptionalParams(
                createdAtStartDate,
                createdAtEndDate,
                updatedAtStartDate,
                updatedAtEndDate,
                accountStatus,
                minValue,
                maxValue
        );

        Pageable pageable = PaginationUtils.generatePagable(page, size, sort);

        Specification<Account> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (accountStatus != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), AccountStatus.valueOf(accountStatus)));
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

    public Account create(User user, String transactionPassword) {
        String encryptedTransactionPassword = pepperPasswordEncoder.encode(transactionPassword);
        Account account = new Account(
                user,
                BigDecimal.ZERO,
                AccountStatus.ACTIVE,
                generateUniqueAccountNumber(),
                encryptedTransactionPassword
        );
        Account savedAccount = accountRepository.save(account);
        accountBudgetAllocationService.create(account);
        return savedAccount;
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

        account.setUpdatedAt(OffsetDateTime.now());
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

    
    
    public AccountResponseDTO toDto(Account account) {
        if (account == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account not found");
        }
        return new AccountResponseDTO(
            account.getUser().getUserProfile().getFirstName(),
            account.getUser().getUserProfile().getLastName(),
            account.getUser().getUserProfile().getEmail(),
            account.getBalance(),
            account.getStatus(),
            account.getAccountNumber(),
            account.getCreatedAt(),
            account.getUpdatedAt()
        );
    }
}
