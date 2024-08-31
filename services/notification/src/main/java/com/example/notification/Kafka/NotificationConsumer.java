package com.example.notification.Kafka;

import com.example.notification.Kafka.DTOs.*;
import com.example.notification.Services.EmailService;
import com.example.notification.Services.NotificationService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {
    private final EmailService emailService;
    private final NotificationService notificationService;
    @KafkaListener(topics = "account-creation")
    public void consumeAccountCreationEvent(AccountCreationEvent accountCreationEvent) throws MessagingException {
        log.info("Consuming the message from account-creation Topic: {}", accountCreationEvent);
        notificationService.saveAccountCreationEvent(accountCreationEvent);
        emailService.sendAccountCreationEmail(accountCreationEvent);
    }


    @KafkaListener(topics = "transaction-confirmation")
    public void consumeTransactionConfirmation(TransactionConfirmation transactionConfirmation) throws MessagingException {
        log.info("Consuming the message from transaction-confirmation Topic: {}", transactionConfirmation);
        notificationService.saveTransactionConfirmation(transactionConfirmation);
        emailService.sendTransactionConfirmationEmail(transactionConfirmation);
    }

    @KafkaListener(topics = "otp-verification")
    public void consumeAuthenticationConfirmation(OtpVerification otpVerification) throws MessagingException {
        log.info("Consuming the message from otp-verification Topic: {}", otpVerification);
        notificationService.saveOtpVerification(otpVerification);
        emailService.sendOtpVerificationEmail(otpVerification);
    }
    @KafkaListener(topics = "user-login-event")
    public void consumeUserLoginEvent(UserLoginEvent userLoginEvent) throws MessagingException {
        log.info("Consuming the message from user-login-event Topic: {}", userLoginEvent);
        notificationService.saveUserLoginEvent(userLoginEvent);
        emailService.sendUserLoginEmail(userLoginEvent);

    }


}
