package com.example.exchange.Models.DTOs;

import java.math.BigDecimal;

public record CryptoRateDTO(
        String baseCurrency,
        String targetCurrency,
        BigDecimal rate
) {
}
