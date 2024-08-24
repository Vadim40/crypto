package com.example.wallet.Models.DTO;

import com.example.wallet.Models.Enum.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class TransactionDTO {
    private Long id;
    private Long accountId;
    private TransactionType transactionType;
    private String tokenSymbol;
    private BigDecimal amount;
    private LocalDate transactionDate;

}

