package com.example.wallet.Services;

import com.example.wallet.Exceptions.NotEnoughTokenAmountException;
import com.example.wallet.Exceptions.TokenNotFoundException;
import com.example.wallet.Models.Token;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.TokenRepository;
import com.example.wallet.Services.Interfaces.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {
    private final TokenRepository tokenRepository;


    @Override
    public void deleteToken(Long id) {
        tokenRepository.deleteById(id);
    }

    @Override
    public Token findTokenById(Long id) {
        return tokenRepository.findById(id).orElseThrow(() -> new TokenNotFoundException("Token not found"));
    }

    @Override
    public Token findTokenBySymbolAndWallet(String symbol, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(symbol, wallet)
                .orElseThrow(() -> new TokenNotFoundException("Token not found: "+symbol));
    }

    @Override
    @Transactional
    public void transferTokens(String symbol, BigDecimal amount, Wallet sourceWallet, Wallet destinationWallet) {
        addTokens(symbol,amount, destinationWallet);
        subtractTokens(symbol, amount, sourceWallet);

    }

    @Override
    public void addTokens(String symbol, BigDecimal amount, Wallet wallet) {
        Token token = getOrCreateToken(symbol, wallet);
        BigDecimal balance = token.getAmount().add(amount);
        token.setAmount(balance);
       tokenRepository.save(token);
    }

    @Override
    public void subtractTokens(String symbol, BigDecimal amount, Wallet wallet) {
        Token token= findTokenBySymbolAndWallet(symbol, wallet);
        BigDecimal balance = token.getAmount().subtract(amount);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughTokenAmountException(String.format(
                    "Insufficient amount of token '%s' . Current balance: %s",
                    symbol,
                    token.getAmount()

            ));
        }
        token.setAmount(balance);
        saveOrDeleteToken(token);
    }

    private void saveOrDeleteToken(Token token) {
        if (token.getAmount().equals(BigDecimal.ZERO)){
            tokenRepository.delete(token);
        }
        tokenRepository.save(token);
    }

    private Token createNewToken(String symbol, Wallet wallet) {
        Token token = new Token();
        token.setSymbol(symbol);
        token.setWallet(wallet);
        token.setAmount(BigDecimal.ZERO);
        return tokenRepository.save(token);
    }
    private Token getOrCreateToken(String symbol, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(symbol, wallet)
                .orElseGet(() -> createNewToken(symbol, wallet));
    }


}
