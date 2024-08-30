package com.example.wallet.Mappers;

import com.example.wallet.Models.*;
import com.example.wallet.Models.DTO.*;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class TransactionMapper {

    private final Map<Class<? extends Transaction>, Function<? super Transaction, TransactionDTO>> mappers = new HashMap<>();

    public TransactionMapper() {
        mappers.put(TransferTransaction.class, transaction -> mapTransferTransactionToDTO((TransferTransaction) transaction));
        mappers.put(DepositTransaction.class, transaction -> mapDepositTransactionToDTO((DepositTransaction) transaction));
        mappers.put(WithdrawalTransaction.class, transaction -> mapWithdrawalTransactionToDTO((WithdrawalTransaction) transaction));
        mappers.put(ReceiveTransaction.class, transaction -> mapReceiveTransactionToDTO((ReceiveTransaction) transaction));
    }

    public TransactionDTO mapTransactionToTransactionDTO(Transaction transaction) {
        Function<? super Transaction, TransactionDTO> mapper = mappers.get(transaction.getClass());
        return mapper.apply(transaction);
    }

    private TransferTransactionDTO mapTransferTransactionToDTO(TransferTransaction transaction) {
        return new TransferTransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getSourceWallet() != null ? transaction.getSourceWallet().getAddress() : null,
                transaction.getDestinationWallet() != null ? transaction.getDestinationWallet().getAddress() : null,
                transaction.getTokenSymbol(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }

    private DepositTransactionDTO mapDepositTransactionToDTO(DepositTransaction transaction) {
        return new DepositTransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getDestinationWallet() != null ? transaction.getDestinationWallet().getAddress() : null,
                transaction.getTokenSymbol(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }

    private WithdrawalTransactionDTO mapWithdrawalTransactionToDTO(WithdrawalTransaction transaction) {
        return new WithdrawalTransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getSourceWallet() != null ? transaction.getSourceWallet().getAddress() : null,
                transaction.getTokenSymbol(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }

    private ReceiveTransactionDTO mapReceiveTransactionToDTO(ReceiveTransaction transaction) {
        return new ReceiveTransactionDTO(
                transaction.getId(),
                transaction.getAccountId(),
                transaction.getTransactionType(),
                transaction.getSourceWallet() != null ? transaction.getSourceWallet().getAddress() : null,
                transaction.getDestinationWallet() != null ? transaction.getDestinationWallet().getAddress() : null,
                transaction.getTokenSymbol(),
                transaction.getAmount(),
                transaction.getTransactionDate()
        );
    }
}

