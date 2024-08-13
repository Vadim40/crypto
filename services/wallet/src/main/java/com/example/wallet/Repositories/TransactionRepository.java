package com.example.wallet.Repositories;

import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
   Optional<List<Transaction>> findTransactionsByTransactionTypeAndAccountId(TransactionType transactionType, Long accountID);

   Optional<List<Transaction>> findTransactionsByTransactionDateAfterAndAccountId(LocalDate localDate, Long accountId);

   Optional<List<Transaction>> findTransactionsByAccountId(Long accountId);
}
