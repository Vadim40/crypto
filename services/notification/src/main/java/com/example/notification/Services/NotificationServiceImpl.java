package com.example.notification.Services;

import com.example.notification.Kafka.DTOs.AccountCreationEvent;
import com.example.notification.Kafka.DTOs.OtpVerification;
import com.example.notification.Kafka.DTOs.TransactionConfirmation;
import com.example.notification.Kafka.DTOs.UserLoginEvent;
import com.example.notification.Models.Enums.NotificationType;
import com.example.notification.Models.Notification;
import com.example.notification.Repositories.NotificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    @Override
    public Notification saveOtpVerification(OtpVerification otpVerification) {
        Notification notification = new Notification();
        notification.setOtpVerification(otpVerification);
        notification.setNotificationType(NotificationType.OTP_VERIFICATION);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification saveAccountCreationEvent(AccountCreationEvent accountCreationEvent) {
        Notification notification = new Notification();
        notification.setAccountCreationEvent(accountCreationEvent);
        notification.setNotificationType(NotificationType.ACCOUNT_CREATION);
        return notificationRepository.save(notification);
    }

    @Override
    public Notification saveUserLoginEvent(UserLoginEvent userLoginEvent) {
        Notification notification = new Notification();
        notification.setUserLoginEvent(userLoginEvent);
        notification.setNotificationType(NotificationType.ACCOUNT_LOGIN);
        return notificationRepository.save(notification);
    }


    @Override
    public Notification saveTransactionConfirmation(TransactionConfirmation transactionConfirmation) {
        Notification notification = new Notification();
        notification.setTransactionConfirmation(transactionConfirmation);
        notification.setNotificationType(NotificationType.TRANSACTION_CONFIRMATION);
        return notificationRepository.save(notification);
    }
}
