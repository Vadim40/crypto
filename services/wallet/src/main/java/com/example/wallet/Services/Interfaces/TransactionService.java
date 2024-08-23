package com.example.wallet.Services.Interfaces;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Models.*;
import com.example.wallet.Models.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {


    Transaction findTransactionById(Long id);

    List<Transaction> findAccountTransactionsByTransactionType(TransactionType transactionType);
    List<Transaction> findAccountTransactionsByTransactionTypeAfterDate(TransactionType transactionType, LocalDate date);
    void transferTokens(String destinationAddress, String symbol, BigDecimal amount);
    void depositTokens(String symbol, BigDecimal amount);
    void withdrawTokens(String symbol, BigDecimal amount);
    void receiveTokens(String sourceAddress, String symbol, BigDecimal amount);
}
