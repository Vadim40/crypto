package com.example.exchange.Models.DTOs;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ConvertRequest(
        @NotNull
        @Pattern(regexp = "^[A-Z]{3,8}$", message = "Currency code must contain 3 to 8 uppercase letters")
        String baseCurrency,

        @NotNull
        @Pattern(regexp = "^[A-Z]{3,8}$", message = "Currency code must contain 3 to 8 uppercase letters")
        String targetCurrency,

        @NotNull
        @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
        BigDecimal amount

) {
}
