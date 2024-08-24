package com.example.wallet.Models.DTO;

import com.example.wallet.Models.Enum.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class DepositTransactionDTO extends TransactionDTO {
    private String walletAddress;

    public DepositTransactionDTO(Long id, Long accountId, TransactionType transactionType,
                                 String walletAddress, String tokenSymbol,
                                 BigDecimal amount, LocalDate transactionDate) {
        super(id, accountId, transactionType, tokenSymbol, amount, transactionDate);
        this.walletAddress = walletAddress;
    }
}