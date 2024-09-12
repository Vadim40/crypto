package com.example.exchange.Services;

import com.example.exchange.Kafka.DTOs.ExchangeConfirmation;
import com.example.exchange.Kafka.ExchangeProducer;
import com.example.exchange.Models.CryptoRate;
import com.example.exchange.Services.Interfaces.AccountService;
import com.example.exchange.Services.Interfaces.CryptoRateDbService;
import com.example.exchange.Services.Interfaces.CryptoRateRedisService;
import com.example.exchange.Services.Interfaces.CryptoRateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class CryptoRateServiceImpl implements CryptoRateService {

    private final CryptoRateRedisService redisService;
    private final CryptoRateDbService dbService;
    private final AccountService accountService;
    private final ExchangeProducer exchangeProducer;

    public CryptoRate findCryptoRate(String baseCurrency, String targetCurrency) {
        return findCryptoRateInRedisOrDb(baseCurrency, targetCurrency);
    }

    private CryptoRate findCryptoRateInRedisOrDb(String baseCurrency, String targetCurrency) {
        return redisService.findCryptoRate(baseCurrency, targetCurrency)
                .map(rate -> new CryptoRate(null, baseCurrency, targetCurrency, rate))
                .orElseGet(() -> dbService.findCryptoRate(baseCurrency, targetCurrency));
    }


    @Override
    public void executeCurrencyConversion(String baseCurrency, String targetCurrency, BigDecimal amount, String email) {
        Long accountId = accountService.findAccountIdByEmail(email);
        BigDecimal rate = findRateInRedisOrDb(baseCurrency, targetCurrency);
        ExchangeConfirmation exchangeConfirmation = createExchangeConfirmation(
                baseCurrency, targetCurrency, rate, amount, accountId);

        exchangeProducer.sendExchangeConfirmation(exchangeConfirmation);
    }

    private BigDecimal findRateInRedisOrDb(String baseCurrency, String targetCurrency) {
        return redisService.findCryptoRate(baseCurrency, targetCurrency)
                .orElseGet(() -> dbService.findCryptoRate(baseCurrency, targetCurrency).getRate());
    }


    private ExchangeConfirmation createExchangeConfirmation(String baseCurrency, String targetCurrency, BigDecimal rate, BigDecimal amount, Long accountId) {
        BigDecimal amountTo = amount.multiply(rate);
        LocalDateTime localDateTime = LocalDateTime.now();
        return new ExchangeConfirmation(
                accountId,
                baseCurrency,
                targetCurrency,
                amount,
                amountTo,
                localDateTime
        );
    }

}
