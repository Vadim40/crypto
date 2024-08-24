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
public class TransferTransactionDTO extends TransactionDTO {
    private String sourceWalletAddress;
    private String destinationWalletAddress;

    public TransferTransactionDTO(Long id, Long accountId, TransactionType transactionType,
                                  String sourceWalletAddress, String destinationWalletAddress,
                                  String tokenSymbol, BigDecimal amount, LocalDate transactionDate) {
        super(id, accountId, transactionType, tokenSymbol, amount, transactionDate);
        this.sourceWalletAddress = sourceWalletAddress;
        this.destinationWalletAddress = destinationWalletAddress;
    }
}