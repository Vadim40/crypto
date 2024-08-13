package com.example.wallet.Models.DTO;

import com.example.wallet.Models.Enum.TransactionType;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TransactionDTO(
        Long id,
        Long accountId,
        TransactionType transactionType,
        String sourceWallet,
        String destinationWallet,
        String tokenType,
        BigDecimal amount,
        LocalDate transactionDate
) {}
