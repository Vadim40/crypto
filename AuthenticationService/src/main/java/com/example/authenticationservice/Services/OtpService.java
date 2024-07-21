package com.example.authenticationservice.Services;

import com.example.authenticationservice.Models.Otp;

public interface OtpService {
    Otp generateOtp(String email);
    boolean verifyOtp(String otp, String username);
    Otp findOtpById(Long id);

    Otp saveOtp(Otp otp);

    Otp updateOtpById(Otp otp, Long id);

    void deleteOtp(Long id);
}
