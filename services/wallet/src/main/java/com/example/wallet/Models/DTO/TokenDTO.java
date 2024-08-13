package com.example.wallet.Models.DTO;

import java.math.BigDecimal;

public record TokenDTO(
        Long id,
        String tokenType,
        BigDecimal amount
) {}