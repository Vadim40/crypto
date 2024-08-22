package com.example.exchange.Models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ExchangeRequest(
        @NotNull
        @Pattern(regexp = "^[A-Z]{3,8}$", message = "Currency code must contain 3 to 8 uppercase letters")
        String baseCurrency,
        @NotNull
        @Pattern(regexp = "^[A-Z]{3,8}$", message = "Currency code must contain 3 to 8 uppercase letters")
        String targetCurrency
) {
}
