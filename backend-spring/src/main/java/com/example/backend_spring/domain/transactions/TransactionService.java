package com.example.backend_spring.domain.transactions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.Account;
import com.example.backend_spring.domain.accounts.AccountService;
import com.example.backend_spring.domain.accounts.AccountType;
import com.example.backend_spring.domain.transactions.dto.TransactionPurchaseCashbackDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionRequestDTO;
import com.example.backend_spring.domain.transactions.dto.TransactionResponseDTO;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    /**
     * TODO - check before transaction if account is deactivated or suspended and inform the trasaction failling reasons as current description problem
     */

    private enum OperationType {
        SUBTRACTION,
        ADDITION
    }

    @Value("${cashback.percentage}")
    private BigDecimal cashbackPercentage;

    @Value("${cashback.minimum-amount}")
    private BigDecimal cashbackMinimumAmount;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    public List<TransactionResponseDTO> findAll() {
        List<TransactionResponseDTO> transactions;

        User currentUser = jwtTokenProviderService.getContextUser();
        
        if (currentUser.isAdmin()) {
            transactions = transactionRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
        } else {
            Account currentUserAccount = accountService.findAccountByUser(currentUser);
            transactions = transactionRepository.findByAccount(currentUserAccount)
                .stream().map(this::toDto).collect(Collectors.toList());
        }

        return transactions;
    }

    public TransactionResponseDTO findByTransactionNumber(UUID transactionNumber) {
        User currentUser = jwtTokenProviderService.getContextUser();

        if (currentUser.isAdmin()) {
            return transactionRepository.findByTransactionNumber(transactionNumber).map(this::toDto)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Transaction not found"
                ));
        }

        Account currentUserAccount = accountService.findAccountByUser(currentUser);
        return transactionRepository.findByTransactionNumberAndAccount(transactionNumber, currentUserAccount).map(this::toDto)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Transaction not found"
            ));
    }

    private TransactionResponseDTO evaluateOperation(
            Account sourceAccount,
            TransactionRequestDTO dto, 
            TransactionType transactionType,
            OperationType operationType,
            String failureMessage
        ) {
        
        Transaction transaction = new Transaction(
            UUID.randomUUID(),
            sourceAccount,
            transactionType,
            dto.description(),
            dto.amount(),
            true
        );
        
        // Less verbose way of cheching if the operation is valid, since amount is always positive
        boolean isOperationValid;
        if(operationType == OperationType.SUBTRACTION) {
            isOperationValid = sourceAccount.getBalance().compareTo(dto.amount()) > 0;
        } else {
            isOperationValid = sourceAccount.getBalance().add(dto.amount()).compareTo(BigDecimal.ZERO) > 0;
        }
        
        if (!isOperationValid) {
            transaction.setSuccess(false);
            transaction.setFailureMessage(failureMessage);
            transactionRepository.save(transaction);
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, failureMessage
            );
        }
        
        sourceAccount.setBalance(
            operationType == OperationType.SUBTRACTION
                ? sourceAccount.getBalance().subtract(dto.amount())
                : sourceAccount.getBalance().add(dto.amount())
        );
        accountService.update(sourceAccount);
        transactionRepository.save(transaction);
        
        return toDto(transaction);
    }

    public TransactionResponseDTO withdraw(TransactionRequestDTO dto) {
        Account sourceAccount = this.getClientAccount();
        return this.evaluateOperation(
            sourceAccount,
            dto, 
            TransactionType.WITHDRAW,
            OperationType.SUBTRACTION,
            "Insufficient funds for withdrawal"
        );
    }

    public TransactionPurchaseCashbackDTO purchase(TransactionRequestDTO dto) {
        Account sourceAccount = this.getClientAccount();
        TransactionResponseDTO purchaseDTO = this.evaluateOperation(
            sourceAccount,
            dto, 
            TransactionType.PURCHASE,
            OperationType.SUBTRACTION,
            "Insufficient funds for purchase"
        );

        // Using a fixed cashback percentage for the sake of simplicity
        // In a real-world scenario, this would be a configurable value
        // and would depend on the account type
        // Giving cashback of `cashbackPercentage` only if the amount is
        // in purchase if equals or greater than `cashbackMinimumAmount`    

        if( sourceAccount.getType() != AccountType.CASHBACK || 
            dto.amount().compareTo(cashbackMinimumAmount) < 0) {
            return new TransactionPurchaseCashbackDTO(purchaseDTO,null);
        }
        BigDecimal cashback = dto.amount().multiply(cashbackPercentage);
        TransactionRequestDTO cashbackRequestDTO = new TransactionRequestDTO(
            cashback,
            String.format("Cashback for purchase %s", purchaseDTO.transactionNumber()),
            null
        );
        TransactionResponseDTO cashbackResponseDTO = this.evaluateOperation(
            sourceAccount,
            cashbackRequestDTO, 
            TransactionType.CASHBACK,
            OperationType.ADDITION,
            "Insufficient funds for cashback"
        );
        return new TransactionPurchaseCashbackDTO(purchaseDTO,cashbackResponseDTO);
    }

    public TransactionResponseDTO deposit(TransactionRequestDTO dto) {
        Account sourceAccount = this.getClientAccount();
        return this.evaluateOperation(
            sourceAccount,
            dto, 
            TransactionType.DEPOSIT,
            OperationType.ADDITION,
            "Insufficient funds for deposit"
        );
    }

    public TransactionResponseDTO transfer(TransactionRequestDTO dto) {
        Account sourceAccount = this.getClientAccount();
        Account destinationAccount = accountService.findAccountByAccountNumber(dto.transferAccountNumber());

        TransactionResponseDTO transferDTO = this.evaluateOperation(
            sourceAccount,
            dto, 
            TransactionType.TRANSFER,
            OperationType.SUBTRACTION,
            "Insufficient funds for transfer"
        );

        destinationAccount.setBalance(
            destinationAccount.getBalance().add(dto.amount())
        );
        accountService.update(destinationAccount);
        
        return transferDTO;
    }

    private Account getClientAccount() {
        User currentUser = jwtTokenProviderService.getContextUser();;
        if (currentUser.isAdmin()) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Admin cannot create transactions"
            );
        }
        return accountService.findAccountByUser(currentUser);
    }

    
    
    private TransactionResponseDTO toDto(Transaction transaction) {
        return new TransactionResponseDTO(
            transaction.getTransactionNumber(),
            transaction.getType(),
            transaction.isSuccess(),
            transaction.getAmount(),
            transaction.getCreatedAt(),
            transaction.getDescription()
        );
    }
}
