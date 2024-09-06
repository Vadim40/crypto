package com.example.wallet.Kafka;

import com.example.wallet.Kafka.DTOs.AccountCreationEvent;
import com.example.wallet.Kafka.DTOs.ExchangeConfirmation;
import com.example.wallet.Services.Interfaces.EventIdHashService;
import com.example.wallet.Services.Interfaces.TransactionService;
import com.example.wallet.Services.Interfaces.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletConsumer {
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final EventIdHashService eventIdHashService;

    @KafkaListener(topics = "account-creation")
    public void consumeAccountCreationConfirmation(AccountCreationEvent accountCreationEvent) {
        log.info("Consuming the message from account-creation Topic: {}", accountCreationEvent);

        if (eventIdHashService.checkAndSaveEvent(accountCreationEvent)) {
            walletService.createAndSaveWallet(accountCreationEvent.id());
        } else {
            log.info("Event {} has already been processed.", accountCreationEvent);
        }
    }

    @KafkaListener(topics = "exchange-topic")
    public void consumeExchangeConfirmation(ExchangeConfirmation exchangeConfirmation) {
        log.info("Consuming the message from exchange-topic Topic: {}", exchangeConfirmation);

        if (eventIdHashService.checkAndSaveEvent(exchangeConfirmation)) {
            transactionService.exchangeTokens(exchangeConfirmation);
        } else {
            log.info("Event  {} has already been processed.", exchangeConfirmation);
        }
    }
}