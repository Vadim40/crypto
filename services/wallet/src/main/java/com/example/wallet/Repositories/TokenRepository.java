package com.example.wallet.Repositories;

import com.example.wallet.Models.Token;
import com.example.wallet.Models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findTokenBySymbolAndWallet(String symbol, Wallet wallet);

}
