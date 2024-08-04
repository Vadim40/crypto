package com.example.wallet.Repositories;

import com.example.wallet.Models.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WalletRepository extends JpaRepository<Wallet,Long> {
    Optional<Wallet> findWalletsByAddress(String address);
    Optional<Wallet> findWalletByAccountId (Long id);
}
