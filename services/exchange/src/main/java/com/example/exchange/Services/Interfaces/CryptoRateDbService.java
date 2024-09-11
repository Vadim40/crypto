package com.example.exchange.Services.Interfaces;

import com.example.exchange.Models.CryptoRate;

import java.math.BigDecimal;

public interface CryptoRateDbService {
    CryptoRate findCryptoRate(String baseCurrency, String targetCurrency);
    CryptoRate saveCryptoRate(CryptoRate rate);

    void updateCryptoRate(CryptoRate rate);
}
