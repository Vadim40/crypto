package com.example.authenticationservice.Controllers;

import com.example.authenticationservice.Mappers.AccountMapper;
import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.DTOs.AccountCreationRequest;
import com.example.authenticationservice.Models.DTOs.AccountDTO;
import com.example.authenticationservice.Services.AccountServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountServiceImpl accountService;
    private final AccountMapper accountMapper;

    @GetMapping("/id/{account-id}")
    public ResponseEntity<Object> findAccountById(@PathVariable("account-id") Long accountId){
        Account account=accountService.findAccountById(accountId);
        AccountDTO accountDTO=accountMapper.mapAccountToAccountDTO(account);
        return new ResponseEntity<>(accountDTO,HttpStatus.OK);
    }
    @GetMapping("/email/{email}")
    public ResponseEntity<Object> findAccountByEmail(@PathVariable("email") String email){
        Account account=accountService.findAccountByEmail(email);
        AccountDTO accountDTO=accountMapper.mapAccountToAccountDTO(account);
        return new ResponseEntity<>(accountDTO,HttpStatus.OK);
    }
    @PostMapping("/sign-up")
    public ResponseEntity<Object> createAccount(@RequestBody @Valid AccountCreationRequest accountCreationRequest) {
        Account account = accountMapper.mapAccountCreationRequestToAccount(accountCreationRequest);
        accountService.saveAccount(account);
        return new ResponseEntity<>(HttpStatus.OK);
    }


    @PutMapping("/check-old-password")
    public ResponseEntity<Object> checkOldPassword(@RequestParam String password) {
        accountService.comparePassword(password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Validated
    @PutMapping("/change-password")
    public ResponseEntity<Object> changePassword(@RequestParam String newPassword) {
        accountService.changePassword(newPassword);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/enable-2fa")
    public ResponseEntity<Object> enableTfa(){
       accountService.enable2fa();
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PutMapping("/disable-2fa")
    public ResponseEntity<Object> disableTfa(){
        accountService.disable2fa();
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
