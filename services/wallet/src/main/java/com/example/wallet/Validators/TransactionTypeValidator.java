package com.example.wallet.Validators;


import com.example.wallet.Models.Enum.TransactionType;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.EnumSet;

public class TransactionTypeValidator implements ConstraintValidator<ValidTransactionType, TransactionType> {

    private static final EnumSet<TransactionType> VALID_TYPES = EnumSet.of(
            TransactionType.TRANSFER,
            TransactionType.RECEIVE,
            TransactionType.DEPOSIT,
            TransactionType.WITHDRAWAL
    );

    @Override
    public void initialize(ValidTransactionType constraintAnnotation) {
    }

    @Override
    public boolean isValid(TransactionType value, ConstraintValidatorContext context) {
        return value == null || VALID_TYPES.contains(value);
    }
}