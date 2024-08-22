package com.example.exchange.Exceptions;

public class CryptoRateNotFoundException extends RuntimeException {
    public CryptoRateNotFoundException(String message) {
        super(message);
    }
}
