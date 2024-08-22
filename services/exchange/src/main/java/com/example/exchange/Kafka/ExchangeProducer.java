package com.example.exchange.Kafka;

import com.example.exchange.Kafka.DTOs.ExchangeConfirmation;
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
public class ExchangeProducer {
    private final KafkaTemplate<String, ExchangeConfirmation> kafkaTemplate;

    public void sendExchangeConfirmation(ExchangeConfirmation exchangeConfirmation) {
        log.info("Sending exchange confirmation for Account ID: {}, converting {} {} to {} {} on {}",
                exchangeConfirmation.accountId(),
                exchangeConfirmation.amountFrom(),
                exchangeConfirmation.symbolFrom(),
                exchangeConfirmation.amountTo(),
                exchangeConfirmation.symbolTo(),
                exchangeConfirmation.timestamp());
        Message<ExchangeConfirmation> message = MessageBuilder
                .withPayload(exchangeConfirmation)
                .setHeader(KafkaHeaders.TOPIC, "exchange-topic")
                .build();
        kafkaTemplate.send(message);
    }

}
