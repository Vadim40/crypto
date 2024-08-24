package com.example.wallet.Models.DTO;

import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Validators.ValidTransactionType;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record TransactionFilterRequest(
        @ValidTransactionType
        TransactionType transactionType,
        @Past(message = "Date must be in the past")
        LocalDate date
) {
}
