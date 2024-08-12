package com.example.authenticationservice.Mappers;

import com.example.authenticationservice.Models.Account;
import com.example.authenticationservice.Models.DTOs.AccountCreationRequest;
import com.example.authenticationservice.Models.DTOs.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDTO mapAccountToAccountDTO(Account account){
        AccountDTO accountDTO=new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setEmail(account.getEmail());
        return accountDTO;
    }
    public Account mapAccountCreationRequestToAccount(AccountCreationRequest accountCreationRequest){
        Account account=new Account();
        account.setEmail(accountCreationRequest.getEmail());
        account.setPassword(accountCreationRequest.getPassword());
        return account;
    }
}
