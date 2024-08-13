package com.example.wallet.Mappers;

import com.example.wallet.Models.DTO.TransactionDTO;
import com.example.wallet.Models.Transaction;
import org.springframework.stereotype.Component;

@Component

public class TransactionMapper {

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getSourceWallet().getAddress(),
                transaction.getDestinationWallet().getAddress(),
                transaction.getTokenType(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }
}



