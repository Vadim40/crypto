package com.example.exchange.Services;

import com.example.exchange.Exceptions.CryptoRateNotFoundException;
import com.example.exchange.Kafka.DTOs.ExchangeConfirmation;
import com.example.exchange.Kafka.ExchangeProducer;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Repositories.CryptoRateRepository;
import com.example.exchange.Services.Interfaces.CryptoRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor

public class CryptoRateServiceImpl implements CryptoRateService {
    private final CryptoRateRepository cryptoRateRepository;
    private final ExchangeProducer exchangeProducer;

    @Override
    public CryptoRate findExchangeRate(String baseCurrency, String targetCurrency) {
        return cryptoRateRepository.findCryptoRateByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                .orElseThrow(() -> new CryptoRateNotFoundException(
                        "Crypto rate for: " + baseCurrency + "/" + targetCurrency + " not found"));
    }

    @Override
    public CryptoRate saveCryptoRate(CryptoRate rate) {
        return cryptoRateRepository.save(rate);
    }

    @Override
    public BigDecimal convertCurrency(String baseCurrency, String targetCurrency, BigDecimal amount) {
        CryptoRate cryptoRate = findExchangeRate(baseCurrency, targetCurrency);
        BigDecimal rate = cryptoRate.getRate();
        BigDecimal convertedAmount = amount.multiply(rate);

        ExchangeConfirmation exchangeConfirmation = new ExchangeConfirmation(baseCurrency, targetCurrency, convertedAmount);
        exchangeProducer.sendExchangeConfirmation(exchangeConfirmation);

        return convertedAmount;
    }
}
