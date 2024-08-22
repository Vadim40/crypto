package com.example.exchange.Services.Interfaces;

import com.example.exchange.Models.CryptoRate;

import java.math.BigDecimal;

public interface CryptoRateService {
    CryptoRate findExchangeRate(String baseCurrency, String targetCurrency);
    CryptoRate saveCryptoRate(CryptoRate rate);
    BigDecimal convertCurrency(String baseCurrency, String targetCurrency, BigDecimal amount);

}
