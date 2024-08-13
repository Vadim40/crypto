package com.example.wallet.Models.DTO;

import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record TransferRequest (

        String destinationAddress,

        @Size(max = 10, message = "Token type should not exceed 10 characters")
        String tokenType,

        BigDecimal amount
) {}