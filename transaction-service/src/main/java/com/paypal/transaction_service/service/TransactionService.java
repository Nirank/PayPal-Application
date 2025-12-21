package com.paypal.transaction_service.service;

import com.paypal.transaction_service.entity.Transaction;

import java.util.List;

public interface TransactionService {

    Transaction createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
}
