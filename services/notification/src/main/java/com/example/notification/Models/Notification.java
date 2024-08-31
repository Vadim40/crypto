package com.example.notification.Models;


import com.example.notification.Kafka.DTOs.AccountCreationEvent;
import com.example.notification.Kafka.DTOs.OtpVerification;
import com.example.notification.Kafka.DTOs.TransactionConfirmation;
import com.example.notification.Kafka.DTOs.UserLoginEvent;
import com.example.notification.Models.Enums.NotificationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {
    @Id
    private Long id;
    private LocalDate localDate;
    private NotificationType notificationType;
    private UserLoginEvent userLoginEvent;
    private AccountCreationEvent accountCreationEvent;
    private TransactionConfirmation transactionConfirmation;
    private OtpVerification otpVerification;
}
