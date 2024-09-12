package com.example.exchange.Services.Interfaces;

import com.example.exchange.Models.CryptoRate;

import java.math.BigDecimal;
import java.util.Optional;

public interface CryptoRateRedisService {
    void  saveCryptoRate(CryptoRate cryptoRate);
    Optional<BigDecimal> findCryptoRate(String baseCurrency, String targetCurrency);
    void deleteAllCryptoRates();
}
