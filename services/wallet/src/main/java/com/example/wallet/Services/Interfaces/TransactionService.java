package com.example.wallet.Services.Interfaces;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Kafka.DTOs.ExchangeConfirmation;
import com.example.wallet.Models.*;
import com.example.wallet.Models.DTO.DepositRequest;
import com.example.wallet.Models.DTO.ReceiveRequest;
import com.example.wallet.Models.DTO.TransferRequest;
import com.example.wallet.Models.DTO.WithdrawalRequest;
import com.example.wallet.Models.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TransactionService {


    Transaction findTransactionById(Long id);

    List<Transaction> findAccountTransactionsByTransactionType(TransactionType transactionType);
    List<Transaction> findAccountTransactionsByTransactionTypeAfterDate(TransactionType transactionType, LocalDate date);
    void transferTokens(TransferRequest request);
    void depositTokens(DepositRequest request);
    void withdrawTokens(WithdrawalRequest request);
    void receiveTokens(ReceiveRequest request);
    void exchangeTokens(ExchangeConfirmation confirmation);
}
