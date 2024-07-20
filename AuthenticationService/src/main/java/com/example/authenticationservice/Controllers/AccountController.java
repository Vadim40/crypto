package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Mappers.AccountMapper;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.DTOs.AccountDTO;
import com.example.authenticationservice.Models.DTOs.ValidationErrorResponse;
import com.example.authenticationservice.Services.AccountServiceImpl;
import com.example.authenticationservice.Services.CustomUserDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final AccountMapper accountMapper;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/sign-up")
    public ResponseEntity<Object> createAccount(@RequestBody @Valid AccountDTO accountDTO,
                                                BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ValidationErrorResponse errorResponse = new ValidationErrorResponse(bindingResult);
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }

        Account account = accountMapper.mapAccountDTOToAccount(accountDTO);
        accountService.saveAccount(account);
        log.info("Account created successfully for email: {}", accountDTO.getEmail());
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/check-old-password")
    public ResponseEntity<Object> CheckOldPassword(@RequestParam String password) {
        Account account = userDetailsService.getAuthenticatedUser();
        if (!passwordEncoder.matches(password, account.getPassword())) {
            log.warn("Password check failed for user: {}", account.getEmail());
            return new ResponseEntity<>("wrong password", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Validated
    @PutMapping("/change-password")
    public ResponseEntity<Object> ChangePassword(@RequestParam String newPassword) {

        Account account = userDetailsService.getAuthenticatedUser();
        account.setPassword(passwordEncoder.encode(newPassword));
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
