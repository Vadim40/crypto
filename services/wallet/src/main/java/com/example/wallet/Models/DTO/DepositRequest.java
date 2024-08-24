package com.example.wallet.Models.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record DepositRequest(
        @Size(max = 6, message = "Token type should not exceed 10 characters")
        String tokenSymbol,
        @DecimalMin(value = "0.00000001", message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
