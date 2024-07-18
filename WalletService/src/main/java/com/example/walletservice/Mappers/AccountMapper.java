package com.example.walletservice.Mappers;

import com.example.walletservice.Models.Account;
import com.example.walletservice.Models.DTOs.AccountDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {
    public AccountDTO mapAccountToAccountDTO(Account account){
        AccountDTO accountDTO=new AccountDTO();
        accountDTO.setEmail(account.getEmail());
        accountDTO.setPassword(account.getPassword());
        return accountDTO;
    }
    public Account mapAccountDTOToAccount(AccountDTO accountDTO){
        Account account=new Account();
        account.setEmail(accountDTO.getEmail());
        account.setPassword(accountDTO.getPassword());
        return account;
    }
}
