package com.example.backend_spring.domain.transactions;


// TODO trocar withdraw por debit
// TODO trocar deposit por credit
// TODO adicionar transaction_credit
// TODO adicionar transaction_debit
public enum TransactionType {
    WITHDRAW,
    DEPOSIT,
    PURCHASE,
    TRANSFER,
    CASHBACK;
}
