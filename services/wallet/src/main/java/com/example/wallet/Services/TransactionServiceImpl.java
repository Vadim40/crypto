package com.example.wallet.Services;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Models.Transaction;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Repositories.TransactionRepository;
import com.example.wallet.Services.Interfaces.TokenService;
import com.example.wallet.Services.Interfaces.TransactionService;
import com.example.wallet.Services.Interfaces.WalletService;
import com.example.wallet.Utils.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TokenService tokenService;
    private final TransactionRepository transactionRepository;
    private final JwtTokenUtils jwtTokenUtils;
    private final WalletService walletService;

    @Override
    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction updateTransaction(Transaction transaction, Long id) {
        transaction.setId(id);
        return transactionRepository.save(transaction);
    }

    @Override
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }

    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id).orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
    }

        @Override
        @Transactional
        public void transferTokens(String addressDestination, String tokenType, BigDecimal amount) {
            String username = getCurrentUsername();
            // will changed in the future
            // retrieveAccountIdFromAuthenticationService();
            Long accountId =1L;

            Wallet sourceWallet = walletService.findWalletByAccountId(accountId);

            Wallet destinationWallet = walletService.findWalletByAddress(addressDestination);

            tokenService.TransferTokens(tokenType,amount, sourceWallet, destinationWallet);

            Transaction transaction = createTransaction(accountId, sourceWallet, destinationWallet, tokenType, amount);
            transactionRepository.save(transaction);
        }

        private String getCurrentUsername() {
            String jwt =  SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
            return jwtTokenUtils.getUsername(jwt);
        }
        private Transaction createTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, String tokenType, BigDecimal amount) {
            Transaction transaction = new Transaction();
            transaction.setAccountId(accountId);
            transaction.setTransactionType(TransactionType.TRANSFER);
            transaction.setSourceWallet(sourceWallet);
            transaction.setDestinationWallet(destinationWallet);
            transaction.setTokenType(tokenType);
            transaction.setAmount(amount);
            transaction.setTransactionDate(LocalDate.now());
            return transaction;
        }
}
