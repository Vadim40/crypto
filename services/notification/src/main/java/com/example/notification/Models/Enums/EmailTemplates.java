package com.example.notification.Models.Enums;

import lombok.Getter;

public enum EmailTemplates {
    OTP_VERIFICATION("otp-verification.html", "Verify your OTP for authentication"),
    USER_LOGIN("user-login.html", "New Login to Your Account"),
    PASSWORD_CHANGE("password-change.html", "Your Password Has Been Changed"),
    NEWSLETTER("newsletter.html", "Latest News and Updates"),
    TRANSACTION_CONFIRMATION("transaction-confirmation.html", "Transaction Confirmation"),
    ACCOUNT_CREATION("account-creation.html", "Welcome to Our Service! Your Account Has Been Created")
    ;

    @Getter
    private final String template;
    @Getter
    private final String subject;

    EmailTemplates(String template, String subject){
        this.template=template;
        this.subject=subject;
    }
}
