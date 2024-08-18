package com.example.wallet.Services.Interfaces;

import com.example.wallet.Models.Wallet;

public interface WalletService {
    Wallet createWallet(Long accountId);

    Wallet updateWallet(Wallet wallet, Long id);

    void deleteWallet(Long id);

    Wallet findWalletById(Long id);

    Wallet findWalletByAddress(String address);

    Wallet findWalletByAccountId(Long id);
}
