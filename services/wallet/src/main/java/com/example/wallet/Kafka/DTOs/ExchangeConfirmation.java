package com.example.wallet.Kafka.DTOs;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeConfirmation(
        Long accountId,
        String symbolFrom,
        String symbolTo,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        LocalDate timestamp
) {
}