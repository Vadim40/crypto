package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpRepository extends JpaRepository<Otp, Long> {

    Optional<Otp> findOtpByAccount(Account account);
}
