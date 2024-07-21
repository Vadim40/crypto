package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.OTPNotFoundException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Otp;
import com.example.authenticationservice.Repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountServiceImpl accountService;

    @Override
    public Otp generateOtp(String email) {
        Random random = new Random();
        int code = 1000 + random.nextInt(9000);
        Account account = accountService.findAccountByEmail(email);
        Otp otp = new Otp();
        //otp.setOtp(passwordEncoder.encode(String.valueOf(code)));
        otp.setOtp(String.valueOf(code));
        otp.setId(account.getId());
        otp.setAccount(account);
        otpRepository.save(otp);
        return otp;
    }

    @Override
    public boolean verifyOtp(String otpToVerify, String email) {
        Account account = accountService.findAccountByEmail(email);
        return account.getOtp().getOtp().equals(otpToVerify);
    }

    @Override
    public Otp findOtpById(Long id) {
        return otpRepository.findById(id).orElseThrow(() -> new OTPNotFoundException("OTP not found"));
    }

    @Override
    public Otp saveOtp(Otp otp) {
        return otpRepository.save(otp);
    }

    @Override
    public Otp updateOtpById(Otp otp, Long id) {
        otp.setId(id);
        return otpRepository.save(otp);
    }

    @Override
    public void deleteOtp(Long id) {
        otpRepository.deleteById(id);
    }
}
