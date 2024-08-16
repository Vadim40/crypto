package com.example.wallet.Mappers;

import com.example.wallet.Models.DTO.TransactionDTO;
import com.example.wallet.Models.Transaction;
import org.springframework.stereotype.Component;

@Component

public class TransactionMapper {

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {

        String sourceWalletAddress = (transaction.getSourceWallet() != null) ? transaction.getSourceWallet().getAddress() : null;
        String destinationWalletAddress = (transaction.getDestinationWallet() != null) ? transaction.getDestinationWallet().getAddress() : null;

        return new TransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                sourceWalletAddress,
                destinationWalletAddress,
                transaction.getTokenType(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }
}



