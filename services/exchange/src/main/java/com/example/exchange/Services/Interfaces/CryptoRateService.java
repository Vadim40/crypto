package com.example.exchange.Services.Interfaces;

import com.example.exchange.Models.CryptoRate;

import java.math.BigDecimal;

public interface CryptoRateService {
    CryptoRate findCryptoRateBySymbol(String symbol);
    BigDecimal convertCurrency(String fromSymbol, String toSymbol, BigDecimal amount);

}
