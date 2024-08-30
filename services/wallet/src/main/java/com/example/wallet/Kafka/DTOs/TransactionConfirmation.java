package com.example.wallet.Kafka.DTOs;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionConfirmation(
        String email,

        LocalDateTime transactionTime,
        String tokenSymbol,
        BigDecimal amount,
        String sourceWallet,
        String destinationWallet
) {
}
