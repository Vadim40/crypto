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

    List<Transaction> findAccountTransactionsByTransactionType(TransactionType transactionType, String email);
    List<Transaction> findAccountTransactionsByTransactionTypeAfterDate(TransactionType transactionType, LocalDate date, String email);
    void transferTokens(TransferRequest request, String email);
    void depositTokens(DepositRequest request,String email);
    void withdrawTokens(WithdrawalRequest request, String email);
    void receiveTokens(ReceiveRequest request, String email);
    void exchangeTokens(ExchangeConfirmation confirmation);
}
