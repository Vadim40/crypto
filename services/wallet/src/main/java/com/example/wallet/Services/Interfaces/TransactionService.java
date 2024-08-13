package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {
    Transaction saveTransaction(Transaction transaction);
    Transaction updateTransaction(Transaction transaction, Long id);
    void deleteTransaction(Long id);
    Transaction findTransactionById(Long id);

    void transferTokens(String addressDestination, String tokenType, BigDecimal amount);
    List<Transaction> findTransactionsByType(TransactionType transactionType);
    List<Transaction> findTransactionsAfterDate(LocalDate localDate);
    List<Transaction> findAllTransactions();
}
