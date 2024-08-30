package com.example.wallet.Kafka;

import com.example.wallet.Kafka.DTOs.AccountCreationEvent;
import com.example.wallet.Kafka.DTOs.ExchangeConfirmation;
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

    @KafkaListener(topics ="account-creation")
    public void consumeAccountCreationConfirmation(AccountCreationEvent accountCreationEvent){
        log.info("Consuming the message from account-creation Topic: {}", accountCreationEvent);
        walletService.createAndSaveWallet(accountCreationEvent.id());
    }
    @KafkaListener(topics ="exchange-topic")
    public void consumeExchangeConfirmation(ExchangeConfirmation exchangeConfirmation){
        log.info("Consuming the message from exchange-topic Topic: {}", exchangeConfirmation);
        transactionService.exchangeTokens(exchangeConfirmation);
    }


}
