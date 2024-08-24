package com.example.wallet.Kafka;

import com.example.wallet.Kafka.DTOs.AccountCreationConfirmation;
import com.example.wallet.Kafka.DTOs.ExchangeConfirmation;
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

    @KafkaListener(topics ="authentication-topic")
    public void consumeAccountCreationConfirmation(AccountCreationConfirmation creationConfirmation){
        log.info("Consuming the message from authentication-topic Topic: {}", creationConfirmation);
        walletService.createAndSaveWallet(creationConfirmation.id());
    }
    @KafkaListener(topics ="exchange-topic")
    public void consumeExchangeConfirmation(ExchangeConfirmation exchangeConfirmation){
        log.info("Consuming the message from authentication-topic Topic: {}", exchangeConfirmation);

    }


}
