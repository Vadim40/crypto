package com.example.wallet.Services;

import com.example.wallet.Exceptions.WalletNotFoundException;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.WalletRepository;
import com.example.wallet.Services.Interfaces.WalletService;
import lombok.RequiredArgsConstructor;
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
        byte[] randomBytes = new byte[20];
        secureRandom.nextBytes(randomBytes);

        StringBuilder sb = new StringBuilder();
        sb.append("0x");
        for (byte b : randomBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
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
        return walletRepository.findById(id).orElseThrow(()->new WalletNotFoundException("Wallet not found"));
    }

    @Override
    public Wallet findWalletByAddress(String address) {
        return walletRepository.findWalletsByAddress(address).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found by this address: " + address));
    }

    @Override
    public Wallet findWalletByAccountId(Long accountId) {
        return walletRepository.findWalletByAccountId(accountId).orElseThrow(() ->
                new WalletNotFoundException("Wallet not found "));
    }


}
