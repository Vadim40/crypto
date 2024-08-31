package com.example.notification.Services;

import com.example.notification.Kafka.DTOs.*;
import com.example.notification.Models.Enums.EmailTemplates;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static com.example.notification.Models.Enums.EmailTemplates.OTP_VERIFICATION;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Async
    public void sendOtpVerificationEmail(OtpVerification otpVerification) throws MessagingException {
        sendEmail(otpVerification.email(), OTP_VERIFICATION, otpVerification);
    }

    @Async
    public void sendAccountCreationEmail(AccountCreationEvent accountCreationEvent) throws MessagingException {
        sendEmail(accountCreationEvent.email(), EmailTemplates.ACCOUNT_CREATION, accountCreationEvent);
    }


    @Async
    public void sendTransactionConfirmationEmail(TransactionConfirmation transactionConfirmation) throws MessagingException {
        sendEmail(transactionConfirmation.email(), EmailTemplates.TRANSACTION_CONFIRMATION, transactionConfirmation);
    }

    @Async
    public void sendUserLoginEmail(UserLoginEvent userLoginEvent) throws MessagingException {
        sendEmail(userLoginEvent.email(), EmailTemplates.USER_LOGIN, userLoginEvent);
    }

    private void sendEmail(String toEmail, EmailTemplates template, Object dto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(
                message, MimeMessageHelper.MULTIPART_MODE_RELATED, StandardCharsets.UTF_8.name());

        messageHelper.setFrom("vadim40@gmail.com");
        messageHelper.setTo(toEmail);
        messageHelper.setSubject(template.getSubject());

        Map<String, Object> variables = new HashMap<>();
        populateVariables(variables, dto);

        Context context = new Context();
        context.setVariables(variables);

        try {
            String htmlTemplate = templateEngine.process(template.getTemplate(), context);
            messageHelper.setText(htmlTemplate, true);
            mailSender.send(message);
            log.info(String.format("Email successfully sent to %s with template %s", toEmail, template.getTemplate()));
        } catch (MessagingException e) {
            log.warn("Cannot send email to {}", toEmail);
            throw e;
        }
    }

    private void populateVariables(Map<String, Object> variables, Object dto) {
        if (dto instanceof OtpVerification otp) {
            variables.put("email", otp.email());
            variables.put("otp", otp.otp());
        } else if (dto instanceof AccountCreationEvent account) {
            variables.put("email", account.email());
            variables.put("id", account.id());
        } else if (dto instanceof TransactionConfirmation transaction) {
            variables.put("email", transaction.email());
            variables.put("transactionTime", transaction.transactionTime());
            variables.put("tokenSymbol", transaction.tokenSymbol());
            variables.put("amount", transaction.amount());
            variables.put("sourceWallet", transaction.sourceWallet());
            variables.put("destinationWallet", transaction.destinationWallet());
        } else if (dto instanceof UserLoginEvent login) {
            variables.put("accountId", login.accountId());
            variables.put("email", login.email());
            variables.put("loginTimestamp", login.loginTimestamp());
            variables.put("remoteIp", login.remoteIp());
        }
    }
}
