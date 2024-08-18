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
    public Token findTokenByTokenTypeAndWallet(String tokenType, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(tokenType, wallet)
                .orElseThrow(() -> new TokenNotFoundException("Token not found: "+tokenType));
    }

    @Override
    @Transactional
    public void transferTokens(String tokenType, BigDecimal amount, Wallet sourceWallet, Wallet destinationWallet) {
        addTokens(tokenType,amount, destinationWallet);
        subtractTokens(tokenType, amount, sourceWallet);

    }

    @Override
    public void addTokens(String tokenType, BigDecimal amount, Wallet wallet) {
        Token token = getOrCreateToken(tokenType, wallet);
        BigDecimal balance = token.getAmount().add(amount);
        token.setAmount(balance);
       tokenRepository.save(token);
    }

    @Override
    public void subtractTokens(String tokenType, BigDecimal amount, Wallet wallet) {
        Token token=findTokenByTokenTypeAndWallet(tokenType, wallet);
        BigDecimal balance = token.getAmount().subtract(amount);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughTokenAmountException(String.format(
                    "Insufficient amount of token '%s' . Current balance: %s",
                    tokenType,
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

    private Token createNewToken(String tokenType, Wallet wallet) {
        Token token = new Token();
        token.setTokenType(tokenType);
        token.setWallet(wallet);
        token.setAmount(BigDecimal.ZERO);
        return tokenRepository.save(token);
    }
    private Token getOrCreateToken(String tokenType, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(tokenType, wallet)
                .orElseGet(() -> createNewToken(tokenType, wallet));
    }


}
