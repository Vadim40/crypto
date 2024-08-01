package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.InvalidOtpException;
import com.example.authenticationservice.Exceptions.OtpExpiredException;
import com.example.authenticationservice.Exceptions.OtpNotFoundException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Otp;
import com.example.authenticationservice.Repositories.OtpRepository;
import com.example.authenticationservice.Services.Interfaces.AccountService;
import com.example.authenticationservice.Services.Interfaces.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;


    @Value("${otp.lifetime}")
    private Duration otpLifetime;


    @Override
    public Otp generateOtp(String email) {
        int otpCode = generateOtpCode();
        Account account = accountService.findAccountByEmail(email);
        Otp otp = createOtp(otpCode, account);
        otpRepository.save(otp);
        log.info("Otp code: " + otpCode);
        return otp;
    }

    private int generateOtpCode() {
        Random random = new Random();
        return 1000 + random.nextInt(9000);
    }

    private Otp createOtp(int otpCode, Account account) {
        Otp otp = new Otp();
        otp.setOtp(passwordEncoder.encode(String.valueOf(otpCode)));
        otp.setId(account.getId());
        otp.setAccount(account);
        otp.setExpiryTime(LocalDateTime.now().plus(otpLifetime));
        return otp;
    }

    @Override
    public void approveOtp(String otpToVerify, String email) {
        Account account = accountService.findAccountByEmail(email);
        checkOtpEnabled(account);
        Otp storedOtp = findOtpById(account.getId());
        checkOtpExpiry(storedOtp);
        validateOtp(otpToVerify, storedOtp);
    }

    private void checkOtpEnabled(Account account) {
        if (!account.isOtpEnabled()) {
            throw new OtpNotFoundException("OTP is not enabled for this account");
        }
    }

    private void checkOtpExpiry(Otp storedOtp) {
        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("Otp has expired");
        }
    }

    private void validateOtp(String otpToVerify, Otp storedOtp) {
        if (!passwordEncoder.matches(otpToVerify, storedOtp.getOtp())) {
            throw new InvalidOtpException("Invalid OTP");
        }
    }


    @Override
    public Otp findOtpById(Long id) {
        return otpRepository.findById(id).orElseThrow(() -> new OtpNotFoundException("OTP not found"));
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
