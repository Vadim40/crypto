package com.example.wallet.Exceptions;

public class NotEnoughTokenAmountException extends RuntimeException {
    public NotEnoughTokenAmountException(String message) {
        super(message);
    }
}
