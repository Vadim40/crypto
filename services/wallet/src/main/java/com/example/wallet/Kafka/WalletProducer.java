package com.example.wallet.Kafka;

import com.example.wallet.Kafka.DTOs.TransactionConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletProducer {
    private final KafkaTemplate<String, TransactionConfirmation> kafkaTemplate;

    public void sendTransactionConfirmation(TransactionConfirmation transactionConfirmation) {
        log.info("Sending exchange confirmation for Email: {}, Transaction Time: {}, Token Symbol: {}, Amount: {}, Source Wallet: {}, Destination Wallet : {}",
                transactionConfirmation.email(),
                transactionConfirmation.transactionTime(),
                transactionConfirmation.tokenSymbol(),
                transactionConfirmation.amount(),
                transactionConfirmation.sourceWallet(),
                transactionConfirmation.destinationWallet());

        Message<TransactionConfirmation> message = MessageBuilder
                .withPayload(transactionConfirmation)
                .setHeader(KafkaHeaders.TOPIC, "exchange-topic")
                .build();
        kafkaTemplate.send(message);
    }

}
