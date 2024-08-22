package com.example.exchange.Kafka.DTOs;

import java.math.BigDecimal;

public record ExchangeConfirmation(
        String symbolFrom,
        String symbolTo,
        BigDecimal amount
) {
}
