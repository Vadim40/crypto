package com.example.wallet.Models.DTO;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ReceiveRequest(
        @Pattern(regexp = "^0x[a-fA-F0-9]{40}$", message = "Invalid wallet address format")
        String sourceAddress,
        @Size(max = 6, message = "Token type should not exceed 10 characters")
        String tokenSymbol,
        @DecimalMin(value = "0.00000001", message = "Amount must be greater than zero")
        BigDecimal amount
) {
}
