package com.example.wallet.Kafka.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ExchangeConfirmation(
        Long accountId,
        String symbolFrom,
        String symbolTo,
        BigDecimal amountFrom,
        BigDecimal amountTo,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDate timestamp
) {
}