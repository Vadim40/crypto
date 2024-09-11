package com.example.exchange.Services;

import com.example.exchange.Exceptions.CryptoRateNotFoundException;
import com.example.exchange.Kafka.DTOs.ExchangeConfirmation;
import com.example.exchange.Kafka.ExchangeProducer;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Repositories.CryptoRateRepository;
import com.example.exchange.Services.Interfaces.AccountService;
import com.example.exchange.Services.Interfaces.CryptoRateDbService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CryptoRateDbServiceImpl implements CryptoRateDbService {
    private final CryptoRateRepository cryptoRateRepository;

    @Override
    public CryptoRate findCryptoRate(String baseCurrency, String targetCurrency) {
        return cryptoRateRepository.findCryptoRateByBaseCurrencyAndTargetCurrency(baseCurrency, targetCurrency)
                .orElseThrow(() -> new CryptoRateNotFoundException(
                        "Crypto rate for: " + baseCurrency + "/" + targetCurrency + " not found"));
    }

    @Override
    public CryptoRate saveCryptoRate(CryptoRate rate) {
        return cryptoRateRepository.save(rate);
    }


    @Override
    public void updateCryptoRate(CryptoRate rate) {
        CryptoRate savedRate= cryptoRateRepository.findCryptoRateByBaseCurrencyAndTargetCurrency(
                rate.getBaseCurrency(), rate.getTargetCurrency()).orElse(rate);
        savedRate.setRate(rate.getRate());
        cryptoRateRepository.save(savedRate);
    }


}
