package com.example.authenticationservice.Services;

import com.example.authenticationservice.Exceptions.OtpNotFoundException;
import com.example.authenticationservice.Exceptions.OtpExpiredException;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Otp;
import com.example.authenticationservice.Repositories.OtpRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;
@Slf4j
@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final AccountServiceImpl accountService;

    @Override
    public Otp generateOtp(String email) {
        Random random = new Random();
        long lifeTimeOtp =5L;
        int code = 1000 + random.nextInt(9000);
        Account account = accountService.findAccountByEmail(email);
        Otp otp = new Otp();
        otp.setOtp(passwordEncoder.encode(String.valueOf(code)));
        otp.setId(account.getId());
        otp.setAccount(account);
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(lifeTimeOtp));
        otpRepository.save(otp);
        log.info("Otp code: "+ code);
        return otp;
    }

    @Override
    public boolean verifyOtp(String otpToVerify, String email) {
        Account account = accountService.findAccountByEmail(email);
        if (!account.isOtpEnabled()) {
            throw new OtpNotFoundException("OTP is not enabled for this account");
        }

        Otp storedOtp = findOtpById(account.getId());

        if (storedOtp.getExpiryTime().isBefore(LocalDateTime.now())) {
            throw new OtpExpiredException("Otp has expired");
        }

        return passwordEncoder.matches(otpToVerify, storedOtp.getOtp());
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
