package com.example.wallet.Repositories;

import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findTransactionByTransactionType(TransactionType transactionType);

    List<Transaction> findTransactionByTransactionDateAfter(LocalDate localDate);
}
