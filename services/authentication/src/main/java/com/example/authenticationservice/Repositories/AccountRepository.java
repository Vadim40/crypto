package com.example.authenticationservice.Repositories;

import com.example.authenticationservice.Models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

     Optional<Account> findAccountByEmail(String email);


     void deleteAccountByEmail(String username);


}
