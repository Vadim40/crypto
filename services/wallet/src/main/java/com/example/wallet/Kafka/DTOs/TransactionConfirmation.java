package com.example.wallet.Kafka.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionConfirmation(
        String email,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime transactionTime,
        String tokenSymbol,
        BigDecimal amount,
        String sourceWallet,
        String destinationWallet
) {

}
