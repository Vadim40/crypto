package com.example.notification.Kafka;

import com.example.notification.Kafka.DTOs.AccountCreationEvent;
import com.example.notification.Kafka.DTOs.ExchangeConfirmation;
import com.example.notification.Kafka.DTOs.OtpVerification;
import com.example.notification.Kafka.DTOs.TransactionConfirmation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    @KafkaListener(topics = "account-creation")
    public void consumeAccountCreationEvent(AccountCreationEvent accountCreationEvent) {
        log.info("Consuming the message from account-creation Topic: {}", accountCreationEvent);
    }

    @KafkaListener(topics = "exchange-confirmation")
    public void consumeExchangeConfirmation(ExchangeConfirmation exchangeConfirmation) {
        log.info("Consuming the message from exchange-confirmation Topic: {}", exchangeConfirmation);
    }

    @KafkaListener(topics = "transaction-confirmation")
    public void consumeTransactionConfirmation(TransactionConfirmation transactionConfirmation) {
        log.info("Consuming the message from transaction-confirmation Topic: {}", transactionConfirmation);
    }

    @KafkaListener(topics = "otp-verification")
    public void consumeAuthenticationConfirmation(OtpVerification otpVerification) {
        log.info("Consuming the message from otp-verification Topic: {}", otpVerification);
    }


}
