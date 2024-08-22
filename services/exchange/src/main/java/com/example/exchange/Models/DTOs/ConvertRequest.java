package com.example.exchange.Models.DTOs;

import java.math.BigDecimal;

public record ConvertRequest(
        String baseCurrency,
        String targetCurrency,
        BigDecimal amount

) {
}
