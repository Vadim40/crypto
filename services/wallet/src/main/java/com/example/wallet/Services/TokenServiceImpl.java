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
    public Token saveToken(Token token) {
        return tokenRepository.save(token);
    }

    @Override
    public Token updateToken(Token token, Long id) {
        token.setId(id);
        return tokenRepository.save(token);
    }

    @Override
    public void deleteToken(Long id) {
        tokenRepository.deleteById(id);

    }

    @Override
    public Token findTokenById(Long id) {
        return tokenRepository.findById(id).orElseThrow(() -> new TokenNotFoundException("Token not found"));
    }

    @Override
    public Token findTokenByTokenTypeAndWallet(String toketType, Wallet wallet) {
        return tokenRepository.findTokenByTokenTypeAndWallet(toketType, wallet)
                .orElseThrow(() -> new TokenNotFoundException("Token not found: "+toketType));
    }

    @Override
    @Transactional
    public void transferTokens(String tokenType, BigDecimal amount, Wallet sourceWallet, Wallet destinationWallet) {
        addTokens(tokenType,amount, destinationWallet);
        subtractTokens(tokenType, amount, sourceWallet);

    }

    @Override
    public void addTokens(String tokenType, BigDecimal amount, Wallet wallet) {
        Token token=findTokenByTokenTypeAndWallet(tokenType, wallet);
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
        tokenRepository.save(token);
    }


}
