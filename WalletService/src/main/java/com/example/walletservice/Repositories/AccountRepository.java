package com.example.walletservice.Repositories;

import com.example.walletservice.Models.Account;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends MongoRepository<Account, String> {

     Optional<Account> findAccountByEmail(String email);


     void deleteAccountByEmail(String username);


}
