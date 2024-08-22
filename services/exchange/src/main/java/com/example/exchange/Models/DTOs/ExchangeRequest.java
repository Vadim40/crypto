package com.example.exchange.Models.DTOs;

public record ExchangeRequest(
        String baseCurrency,
        String targetCurrency
) {
}
