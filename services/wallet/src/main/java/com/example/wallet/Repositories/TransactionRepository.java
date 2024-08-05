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
   Optional<List<Transaction>> findTransactionByTransactionType(TransactionType transactionType);

   Optional<List<Transaction>> findTransactionByTransactionDateAfter(LocalDate localDate);
}
