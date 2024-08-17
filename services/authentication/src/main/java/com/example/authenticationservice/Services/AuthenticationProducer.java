package com.example.authenticationservice.Services;

import com.example.authenticationservice.Models.DTOs.AccountCreationConfirmation;
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
public class AuthenticationProducer {
    private final KafkaTemplate<String, AccountCreationConfirmation> kafkaTemplate;
    public void sendAccountCreationConfirmation(AccountCreationConfirmation accountCreationConfirmation){
        log.info("Sending account creation event with ID: {} and email: {}"
                , accountCreationConfirmation.id(), accountCreationConfirmation.email());
        Message<AccountCreationConfirmation> message= MessageBuilder
                .withPayload(accountCreationConfirmation)
                .setHeader(KafkaHeaders.TOPIC,"authentication-topic")
                .build();
        kafkaTemplate.send(message);
    }
}
