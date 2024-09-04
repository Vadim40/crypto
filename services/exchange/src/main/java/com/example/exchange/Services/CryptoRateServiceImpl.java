package com.example.exchange.Services;

import com.example.exchange.Exceptions.CryptoRateNotFoundException;
import com.example.exchange.Kafka.DTOs.ExchangeConfirmation;
import com.example.exchange.Kafka.ExchangeProducer;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Models.DTOs.AccountResponse;
import com.example.exchange.Repositories.CryptoRateRepository;
import com.example.exchange.Services.Interfaces.AccountService;
import com.example.exchange.Services.Interfaces.CryptoRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor

public class CryptoRateServiceImpl implements CryptoRateService {
    private final CryptoRateRepository cryptoRateRepository;
    private final ExchangeProducer exchangeProducer;
    private final AccountService accountService;

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
    public void executeCurrencyConversion(String baseCurrency, String targetCurrency, BigDecimal amount, String email) {
        AccountResponse accountResponse = accountService.findAccountByEmail(email);
        ExchangeConfirmation exchangeConfirmation = createExchangeConfirmation(
                baseCurrency, targetCurrency, amount, accountResponse);
        exchangeProducer.sendExchangeConfirmation(exchangeConfirmation);
    }

    @Override
    public void updateCryptoRate(CryptoRate rate) {
        CryptoRate savedRate= cryptoRateRepository.findCryptoRateByBaseCurrencyAndTargetCurrency(
                rate.getBaseCurrency(), rate.getTargetCurrency()).orElse(rate);
        savedRate.setRate(rate.getRate());
        cryptoRateRepository.save(savedRate);
    }

    private ExchangeConfirmation createExchangeConfirmation(String baseCurrency, String targetCurrency, BigDecimal amount, AccountResponse accountResponse) {
        CryptoRate cryptoRate = findCryptoRate(baseCurrency, targetCurrency);
        BigDecimal rate = cryptoRate.getRate();
        BigDecimal amountTo = amount.multiply(rate);
        LocalDate localDate = LocalDate.now();
        return new ExchangeConfirmation(
                accountResponse.id(),
                baseCurrency,
                targetCurrency,
                amount,
                amountTo,
                localDate
        );
    }
}
