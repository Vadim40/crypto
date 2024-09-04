package com.example.exchange.Services.Interfaces;

import com.example.exchange.Models.CryptoRate;

import java.math.BigDecimal;

public interface CryptoRateService {
    CryptoRate findCryptoRate(String baseCurrency, String targetCurrency);
    CryptoRate saveCryptoRate(CryptoRate rate);
    void executeCurrencyConversion(String baseCurrency, String targetCurrency, BigDecimal amount, String email);

    void updateCryptoRate(CryptoRate rate);
}
