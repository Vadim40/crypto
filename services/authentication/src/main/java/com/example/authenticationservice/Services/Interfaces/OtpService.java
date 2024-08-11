package com.example.authenticationservice.Services.Interfaces;

import com.example.authenticationservice.Models.Otp;

public interface OtpService {
    String generateOtp(String email);
    void approveOtp(String otp, String username);
    Otp findOtpById(Long id);

    Otp saveOtp(Otp otp);

    Otp updateOtpById(Otp otp, Long id);

    void deleteOtp(Long id);

}
