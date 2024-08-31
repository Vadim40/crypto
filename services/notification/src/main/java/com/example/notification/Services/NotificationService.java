package com.example.notification.Services;

import com.example.notification.Kafka.DTOs.AccountCreationEvent;
import com.example.notification.Kafka.DTOs.OtpVerification;
import com.example.notification.Kafka.DTOs.TransactionConfirmation;
import com.example.notification.Kafka.DTOs.UserLoginEvent;
import com.example.notification.Models.Notification;

public interface NotificationService {
    Notification saveOtpVerification(OtpVerification otpVerification);

    Notification saveAccountCreationEvent(AccountCreationEvent accountCreationEvent);

    Notification saveUserLoginEvent(UserLoginEvent userLoginEvent);

    Notification saveTransactionConfirmation(TransactionConfirmation transactionConfirmation);


}
