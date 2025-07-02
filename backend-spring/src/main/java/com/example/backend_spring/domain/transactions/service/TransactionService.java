package com.example.backend_spring.domain.transactions.service;

import com.example.backend_spring.domain.accounts.dto.BudgetCategoryDTO;
import com.example.backend_spring.domain.accounts.model.BudgetCategory;
import com.example.backend_spring.domain.accounts.repository.BudgetCategoryRepository;
import com.example.backend_spring.domain.accounts.utils.AccountStatus;
import com.example.backend_spring.domain.transactions.dto.*;
import com.example.backend_spring.domain.transactions.model.TransactionRequest;
import com.example.backend_spring.domain.transactions.model.TransferTransaction;
import com.example.backend_spring.domain.transactions.repository.TransactionRequestRepository;
import com.example.backend_spring.domain.transactions.repository.TransferTransactionRepository;
import com.example.backend_spring.domain.transactions.utils.TransactionOperationType;
import com.example.backend_spring.domain.transactions.utils.TransactionStatus;
import com.example.backend_spring.domain.users.utils.UserRole;
import com.example.backend_spring.security.encoder.PepperPasswordEncoder;
import com.example.backend_spring.utils.PaginationUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.backend_spring.domain.accounts.model.Account;
import com.example.backend_spring.domain.accounts.service.AccountService;
import com.example.backend_spring.domain.transactions.model.Transaction;
import com.example.backend_spring.domain.transactions.repository.TransactionRepository;
import com.example.backend_spring.domain.users.model.User;
import com.example.backend_spring.security.jwt.JwtTokenProviderService;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Autowired
    private JwtTokenProviderService jwtTokenProviderService;

    @Autowired
    private PepperPasswordEncoder pepperPasswordEncoder;

    @Autowired
    private BudgetCategoryRepository budgetCategoryRepository;

    @Autowired
    private TransactionRequestRepository transactionRequestRepository;

    @Autowired
	private TransferTransactionRepository transferTransactionRepository;

	private final List<String> ALLOWED_SORT_FIELDS = Arrays.asList(
			"id",
			"transactionNumber",
			"operationType",
			"description",
			"status",
			"amount",
			"createdAt"
	);

	private void validateFindAllOptionalParams(
			String accountNumbers,
			OffsetDateTime startDate,
			OffsetDateTime endDate,
			String operationType,
			String transactionStatus,
			BigDecimal minValue,
			BigDecimal maxValue
	) {
		if (accountNumbers != null && !accountNumbers.isEmpty()) {
			List<String> accountNumberList = List.of(accountNumbers.split(","));
			for (String accNum : accountNumberList) {
				if (accNum.isBlank()) {
					throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Account number Page");
				}
			}
			List<String> foundAccounts = accountService.findAllByAccountNumberIn(accountNumberList)
					.stream().map(Account::getAccountNumber).toList();
			if (foundAccounts.size() != accountNumberList.size()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "One or more accountNumbers invalids");
			}
		}
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "startDate can't be after endDate");
		}
		if (operationType != null) {
			try {
				TransactionOperationType.valueOf(operationType);
			} catch (IllegalArgumentException e) {
				String acceptableValues = String.join(", ", Arrays.stream(TransactionOperationType.values())
						.map(Enum::name)
						.toList());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"operationType invalid. Acceptable values are: " + acceptableValues);
			}
		}
		if (transactionStatus != null) {
			try {
				TransactionStatus.valueOf(transactionStatus);
			} catch (IllegalArgumentException e) {
				String acceptableValues = String.join(", ", Arrays.stream(TransactionStatus.values())
						.map(Enum::name)
						.toList());
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"transactionStatus invalid. Acceptable values are: " + acceptableValues);
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

    public Page<TransactionResponseDTO> findByFilters(
			int page,
			int size,
			String sort,
			String accountNumbers,
			OffsetDateTime startDate,
			OffsetDateTime endDate,
			String operationType,
			String transactionStatus,
			BigDecimal minValue,
			BigDecimal maxValue) {

		PaginationUtils.validatePaginationParams(page, size, sort, ALLOWED_SORT_FIELDS);
		validateFindAllOptionalParams(
				accountNumbers,
				startDate,
				endDate,
				operationType,
				transactionStatus,
				minValue,
				maxValue
		);

		Pageable pageable = PaginationUtils.generatePagable(page, size, sort);

		Specification<Transaction> specification = (root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();

			if (accountNumbers != null && !accountNumbers.isEmpty()) {
				List<String> accountNumberList = List.of(accountNumbers.split(","));
				predicates.add(root.get("account").get("accountNumber").in(accountNumberList));
			}
			if (startDate != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDate));
			}
			if (endDate != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDate));
			}
			if (operationType != null) {
				predicates.add(criteriaBuilder.equal(root.get("operationType"), TransactionOperationType.valueOf(operationType)));
			}
			if (transactionStatus != null) {
				predicates.add(criteriaBuilder.equal(root.get("status"), TransactionStatus.valueOf(transactionStatus)));
			}
			if (minValue != null) {
				predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("amount"), minValue));
			}
			if (maxValue != null) {
				predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("amount"), maxValue));
			}

			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		};

        return transactionRepository.findAll(specification, pageable).map(this::toDto);
    }

	public Page<TransactionResponseDTO> findByFiltersAndContextUser(
			int page,
			int size,
			String sort,
			OffsetDateTime startDate,
			OffsetDateTime endDate,
			String operationTypes,
			String status,
			BigDecimal minValue,
			BigDecimal maxValue
	) {
		User currentUser = jwtTokenProviderService.getContextUser();
		if (currentUser.getRole() != UserRole.CLIENT) {
			throw new ResponseStatusException(
					HttpStatus.FORBIDDEN, "Only clients can view their transactions"
			);
		}
		Account contextAccount = accountService.findAccountByUser(currentUser);
		String accountNumber = contextAccount.getAccountNumber();
		return this.findByFilters(
				page,
				size,
				sort,
				accountNumber,
				startDate,
				endDate,
				operationTypes,
				status,
				minValue,
				maxValue
		);
	}

    public TransactionResponseDTO findByTransactionNumber(UUID transactionNumber) {
        User currentUser = jwtTokenProviderService.getContextUser();

        if (currentUser.getRole() == UserRole.ADMIN) {
            return transactionRepository.findByTransactionNumber(transactionNumber).map(this::toDto)
                    .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Transaction not found"
                    ));
        }
        else if(currentUser.getRole() == UserRole.CLIENT) {
            Account currentUserAccount = accountService.findAccountByUser(currentUser);
            return transactionRepository.findByTransactionNumberAndAccount(
                        transactionNumber,
                            currentUserAccount
                    ).map(this::toDto)
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.NOT_FOUND, "Transaction not found"
                    ));
        }
        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Only admins and clients can view transactions"
        );
    }

    private boolean isDebitOperation(TransactionOperationType operationType) {
        return operationType == TransactionOperationType.DEBIT ||
                operationType == TransactionOperationType.TRANSFER_DEBIT ||
                operationType == TransactionOperationType.PURCHASE;
    }

    private User validadeAndRetreiveContextUser() {
        User user = jwtTokenProviderService.getContextUser();
        if(user.getRole() != UserRole.CLIENT) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN, "Only clients can perform transactions"
            );
        }
        return user;
    }

    private void validateAccountIsActive(Account account) {
        if (account.getStatus() != AccountStatus.ACTIVE) {
            throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "Account is not active"
            );
        }
    }

    private void validateTransactionPassword(Account account, String transactionPassword) {
        String dbEncryptedTransactionPassword = account.getTransactionPassword();
        if (!pepperPasswordEncoder.matches(transactionPassword, dbEncryptedTransactionPassword)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Transaction password did not match");
        }
    }

    private void validateAccountFunds(Account account, BigDecimal amount, TransactionOperationType operationType) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Amount must be greater than zero");
        }

        if (this.isDebitOperation(operationType) &&
                account.getBalance().compareTo(amount) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient funds for debit operation");
        }
    }

    private Account validateSourceAccount(TransactionRequestDTO dto, TransactionOperationType operationType) {
        User contextUser = this.validadeAndRetreiveContextUser();
        Account account = accountService.findAccountByUser(contextUser);

        validateAccountIsActive(account);
        validateTransactionPassword(account, dto.transactionPassword());
        validateAccountFunds(account, dto.amount(), operationType);

        return account;
    }

    private Account validateDestinationAccount(String accountNumber) {
        Account account = accountService.findAccountByAccountNumber(accountNumber);
        validateAccountIsActive(account);
        return account;
    }

    private BudgetCategory getBudgetCategory(UUID budgetCategoryId) {
        return budgetCategoryRepository.findById(budgetCategoryId)
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Budget category not found"
            ));
    }

    public TransactionResponseDTO withdraw(TransactionRequestDTO dto) {
        TransactionOperationType operationType = TransactionOperationType.DEBIT;
        Account account = this.validateSourceAccount(dto, operationType);
        BudgetCategory budgetCategory = this.getBudgetCategory(dto.budgetCategoryId());

        // Transaction is approved, proceed with the operation
        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                account,
                budgetCategory,
                operationType,
                dto.description(),
                dto.amount(),
                TransactionStatus.APPROVED
        );
        transactionRepository.save(transaction);

        account.setBalance(
                account.getBalance().subtract(dto.amount())
        );
        accountService.update(account);
        return this.toDto(transaction);
    }

    public TransactionResponseDTO depositRequest(TransactionRequestDTO dto) {
        TransactionOperationType operationType = TransactionOperationType.CREDIT;
        Account account = this.validateSourceAccount(dto, operationType);
        BudgetCategory budgetCategory = this.getBudgetCategory(dto.budgetCategoryId());

        Transaction transaction = new Transaction(
                UUID.randomUUID(),
                account,
                budgetCategory,
                operationType,
                dto.description(),
                dto.amount(),
                TransactionStatus.PENDING
        );
        transactionRepository.save(transaction);

        User contextUser = this.validadeAndRetreiveContextUser();
        TransactionRequest transactionRequest = new TransactionRequest(
                transaction,
                contextUser
        );
        transactionRequestRepository.save(transactionRequest);
        return this.toDto(transaction);
    }

    public TransactionTransferResponseDTO transfer(TransactionTransferRequestDTO transferDto) {
        TransactionRequestDTO dto = transferDto.sourceTransaction();

        // Validates source account
        TransactionOperationType sourceOperationType =
                TransactionOperationType.TRANSFER_DEBIT;
        Account sourceAccount = this.validateSourceAccount(dto, sourceOperationType);
        BudgetCategory budgetCategory = this.getBudgetCategory(dto.budgetCategoryId());

        // Validates destination account
        TransactionOperationType destinationOperationType =
                TransactionOperationType.TRANSFER_CREDIT;
        Account destinationAccount = this.validateDestinationAccount(
                transferDto.transferAccountNumber());


        Transaction sourceTransaction = new Transaction(
                UUID.randomUUID(),
                sourceAccount,
                budgetCategory,
                sourceOperationType,
                dto.description(),
                dto.amount(),
                TransactionStatus.APPROVED
        );
        Transaction destinationTransaction = new Transaction(
                UUID.randomUUID(),
                destinationAccount,
                destinationOperationType,
                dto.description(),
                dto.amount(),
                TransactionStatus.APPROVED
        );
        transactionRepository.save(sourceTransaction);
        transactionRepository.save(destinationTransaction);

        TransferTransaction transferTransaction = new TransferTransaction(
                sourceTransaction,
                destinationTransaction
        );
        transferTransactionRepository.save(transferTransaction);

        sourceAccount.setBalance(
                sourceAccount.getBalance().subtract(dto.amount())
        );
        destinationAccount.setBalance(
                destinationAccount.getBalance().add(dto.amount())
        );
        accountService.update(sourceAccount);
        accountService.update(destinationAccount);

        return this.toTransferDto(sourceTransaction, destinationAccount);
    }

	public TransactionResponseDTO purchase(TransactionRequestDTO dto) {
		TransactionOperationType operationType = TransactionOperationType.PURCHASE;
		Account account = this.validateSourceAccount(dto, operationType);
		BudgetCategory budgetCategory = this.getBudgetCategory(dto.budgetCategoryId());

		// Transaction is approved, proceed with the operation
		Transaction transaction = new Transaction(
				UUID.randomUUID(),
				account,
				budgetCategory,
				operationType,
				dto.description(),
				dto.amount(),
				TransactionStatus.APPROVED
		);
		transactionRepository.save(transaction);

		account.setBalance(
				account.getBalance().subtract(dto.amount())
		);
		accountService.update(account);
		return this.toDto(transaction);
	}

	private Transaction validateTransactionExists(UUID transactionNumber) {
		return transactionRepository.findByTransactionNumber(transactionNumber)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Transaction not found"
			));
	}

	private TransactionRequest validatePendingTransactionRequest(Transaction transaction) {
		if (transaction.getStatus() != TransactionStatus.PENDING) {
			throw new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Transaction provided is not pending"
			);
		}
		return transactionRequestRepository.findByTransaction(transaction)
			.orElseThrow(() -> new ResponseStatusException(
				HttpStatus.NOT_FOUND, "Transaction request not found"
			));
	}

	private Account validateAccountByTransaction(Transaction transaction) {
		Account account = transaction.getAccount();
		validateAccountIsActive(account);
		return account;
	}

	private User validateContextAdminUser() {
		User user = jwtTokenProviderService.getContextUser();
		if (user.getRole() != UserRole.ADMIN) {
			throw new ResponseStatusException(
				HttpStatus.FORBIDDEN, "Only admins can approve or reprove deposit requests"
			);
		}
		return user;
	}

	public TransactionResponseDTO reviewTransactionRequest(
			UUID transactionNumber,
			String rejectionMessage,
			TransactionStatus transactionStatus
	) {
		if(transactionStatus == TransactionStatus.PENDING) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Decision must be made, Reviewed transaction status cannot be PENDING"
			);
		}
		if(	transactionStatus == TransactionStatus.REJECTED &&
				(rejectionMessage == null || rejectionMessage.isBlank())) {
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Rejection message is required"
			);
		}
		Transaction transaction = this.validateTransactionExists(transactionNumber);
		Account destinationAccount = this.validateAccountByTransaction(transaction);
		TransactionRequest transactionRequest = this.validatePendingTransactionRequest(transaction);
		User adminUser = this.validateContextAdminUser();

		transactionRequest.setReviewedAt(OffsetDateTime.now());
		transactionRequest.setStatus(transactionStatus);
		transactionRequest.setReviewedBy(adminUser);
		transactionRequestRepository.save(transactionRequest);

		transaction.setStatus(transactionStatus);
		transaction.setRejectionMessage(rejectionMessage);
		transactionRepository.save(transaction);

		if(transactionStatus == TransactionStatus.APPROVED) {
			destinationAccount.setBalance(
					destinationAccount.getBalance().add(transaction.getAmount())
			);
			accountService.update(destinationAccount);
		}

		return this.toDto(transaction);
	}

    private TransactionResponseDTO toDto(Transaction transaction) {
        BudgetCategory budgetCategory = transaction.getBudgetCategory();
        return new TransactionResponseDTO(
                transaction.getTransactionNumber(),
                transaction.getCreatedAt(),
                transaction.getOperationType(),
                transaction.getStatus(),
                transaction.getDescription(),
                transaction.getAmount(),
                transaction.getRejectionMessage(),
				budgetCategory == null ? null : new BudgetCategoryDTO(
                        budgetCategory.getName(),
                        budgetCategory.getId()
                )
        );
    }

    private TransactionTransferResponseDTO toTransferDto(Transaction transaction, Account destinationAccount) {
        if (transaction == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Transaction not found"
            );
        }
        if (destinationAccount == null) {
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Destination account not found"
            );
        }
        return new TransactionTransferResponseDTO(
                this.toDto(transaction),
                new TransferReceiverInfoDTO(
                        destinationAccount.getAccountNumber(),
                        destinationAccount.getUser().getUserProfile().getFirstName(),
                        destinationAccount.getUser().getUserProfile().getLastName()

                )
        );
    }

}
