package com.example.authenticationservice.Mappers;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.DTOs.AccountCreationRequest;
import com.example.authenticationservice.Models.DTOs.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDTO mapAccountToAccountDTO(Account account){
        return new AccountDTO(account.getId(), account.getEmail());
    }
    public Account mapAccountCreationRequestToAccount(AccountCreationRequest accountCreationRequest){
        Account account=new Account();
        account.setEmail(accountCreationRequest.email());
        account.setPassword(accountCreationRequest.password());
        return account;
    }
}
