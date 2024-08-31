package com.example.authenticationservice.Kafka;

import com.example.authenticationservice.Kafka.DTOs.AccountCreationEvent;
import com.example.authenticationservice.Kafka.DTOs.UserLoginEvent;
import com.example.authenticationservice.Kafka.DTOs.PasswordChangeEvent;
import com.example.authenticationservice.Kafka.DTOs.OtpVerification;
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

    private final KafkaTemplate<String, AccountCreationEvent> accountCreationEventKafkaTemplate;
    private final KafkaTemplate<String, UserLoginEvent> userLoginEventKafkaTemplate;
    private final KafkaTemplate<String, PasswordChangeEvent> passwordChangeEventKafkaTemplate;
    private final KafkaTemplate<String, OtpVerification> otpVerificationKafkaTemplate;

    public void sendAccountCreationConfirmation(AccountCreationEvent accountCreationEvent) {
        log.info("Sending account creation event with ID: {} and email: {}",
                accountCreationEvent.id(), accountCreationEvent.email());
        Message<AccountCreationEvent> message = MessageBuilder
                .withPayload(accountCreationEvent)
                .setHeader(KafkaHeaders.TOPIC, "account-creation")
                .build();
        accountCreationEventKafkaTemplate.send(message);
    }

    public void sendUserLoginEvent(UserLoginEvent userLoginEvent) {
        log.info("Sending user login event with account ID: {} and email: {}",
                userLoginEvent.accountId(), userLoginEvent.email());
        Message<UserLoginEvent> message = MessageBuilder
                .withPayload(userLoginEvent)
                .setHeader(KafkaHeaders.TOPIC, "user-login-event")
                .build();
        userLoginEventKafkaTemplate.send(message);
    }

    public void sendPasswordChangeEvent(PasswordChangeEvent passwordChangeEvent) {
        log.info("Sending password change event with account ID: {} and email: {}",
                passwordChangeEvent.accountId(), passwordChangeEvent.email());
        Message<PasswordChangeEvent> message = MessageBuilder
                .withPayload(passwordChangeEvent)
                .setHeader(KafkaHeaders.TOPIC, "password-change-event")
                .build();
        passwordChangeEventKafkaTemplate.send(message);
    }

    public void sendOtpVerification(OtpVerification otpVerification) {
        log.info("Sending OTP verification event with account ID: {} and email: {}",
                otpVerification.accountId(), otpVerification.email());
        Message<OtpVerification> message = MessageBuilder
                .withPayload(otpVerification)
                .setHeader(KafkaHeaders.TOPIC, "otp-verification")
                .build();
        otpVerificationKafkaTemplate.send(message);
    }
}
