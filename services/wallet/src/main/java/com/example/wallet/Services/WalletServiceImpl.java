package com.example.wallet.Services;

import com.example.wallet.Exceptions.WalletNotFoundException;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.WalletRepository;
import com.example.wallet.Services.Interfaces.WalletService;
import lombok.RequiredArgsConstructor;
import org.bitcoinj.core.Base58;
import org.bouncycastle.jcajce.provider.digest.SHA256;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {
    private final WalletRepository walletRepository;

    @Override
    public Wallet saveWallet(Wallet wallet) {
        String address=generateAddress();
        wallet.setAddress(address);
        return walletRepository.save(wallet);
    }

    private String generateAddress() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);


        byte[] hash = new SHA256.Digest().digest(randomBytes);

        return Base58.encode(hash);
    }

    @Override
    public Wallet updateWallet(Wallet wallet, Long id) {
        wallet.setId(id);
        return walletRepository.save(wallet);
    }

    @Override
    public void deleteWallet(Long id) {
        walletRepository.deleteById(id);

    }

    @Override
    public Wallet findWalletById(Long id) {
        return walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("wallet not found"));
    }

    @Override
    public Wallet findWalletByAddress(String address) {
        return walletRepository.findWalletsByAddress(address).orElseThrow(() ->
                new WalletNotFoundException("wallet not found by this address: " + address));
    }


}
