package com.example.wallet.Services;

import com.example.wallet.Exceptions.TransactionNotFoundException;
import com.example.wallet.Kafka.DTOs.ExchangeConfirmation;
import com.example.wallet.Kafka.DTOs.TransactionConfirmation;
import com.example.wallet.Kafka.WalletProducer;
import com.example.wallet.Models.*;
import com.example.wallet.Models.DTO.*;
import com.example.wallet.Models.Enum.TransactionType;
import com.example.wallet.Repositories.*;
import com.example.wallet.Services.Interfaces.AccountService;
import com.example.wallet.Services.Interfaces.TokenService;
import com.example.wallet.Services.Interfaces.TransactionService;
import com.example.wallet.Services.Interfaces.WalletService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TokenService tokenService;
    private final WalletService walletService;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final WalletProducer walletProducer;

    @Override
    public Transaction findTransactionById(Long id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found by id: " + id));
    }

    @Override
    public List<Transaction> findAccountTransactionsByTransactionType(TransactionType transactionType) {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByTransactionTypeAndAccountId(transactionType, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found for type: " + transactionType));
    }

    @Override
    public List<Transaction> findAccountTransactionsByTransactionTypeAfterDate(TransactionType transactionType, LocalDate date) {
        Long accountId = accountService.getCurrentAccount().id();
        return transactionRepository.findTransactionsByTransactionTypeAndTransactionDateAfterAndAccountId(transactionType, date, accountId)
                .orElseThrow(() -> new TransactionNotFoundException("Transactions not found for type: " + transactionType + ", after date: " + date));
    }

    @Transactional
    @Override
    public void transferTokens(TransferRequest request) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenTransfer(accountResponse, request);
    }

    @Transactional
    @Override
    public void depositTokens(DepositRequest request) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenDeposit(accountResponse, request);
    }

    @Transactional
    @Override
    public void withdrawTokens(WithdrawalRequest request) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenWithdrawal(accountResponse, request);
    }

    @Transactional
    @Override
    public void receiveTokens(ReceiveRequest request) {
        AccountResponse accountResponse = accountService.getCurrentAccount();
        processTokenReceive(accountResponse, request);
    }

    @Transactional
    @Override
    public void exchangeTokens(ExchangeConfirmation confirmation) {
        processExchangeTokenTransfer(confirmation);
    }

    private void processExchangeTokenTransfer(ExchangeConfirmation confirmation) {
        Wallet wallet = walletService.findWalletByAccountId(confirmation.accountId());
        tokenService.subtractTokens(confirmation.symbolFrom(), confirmation.amountFrom(), wallet);
        tokenService.addTokens(confirmation.symbolTo(), confirmation.amountTo(), wallet);
        
        createAndSaveExchangeTransaction(wallet, confirmation);
    }



    private void processTokenTransfer(AccountResponse accountResponse, TransferRequest request) {
        Wallet sourceWallet = walletService.findWalletByAccountId(accountResponse.id());
        Wallet destinationWallet = walletService.findWalletByAddress(request.destinationAddress());

        tokenService.transferTokens(request.tokenSymbol(), request.amount(), sourceWallet, destinationWallet);
        createAndSaveTransferTransaction(accountResponse.id(), sourceWallet, destinationWallet, request);

       sendTransactionConfirmation(sourceWallet.getAddress(),accountResponse.email()
               ,request.tokenSymbol(),
               request.amount(),
               destinationWallet.getAddress());
    }

    private void sendTransactionConfirmation(String sourceAddress, String email, String tokenSymbol, BigDecimal amount, String destinationAddress) {
        TransactionConfirmation transactionConfirmation=new TransactionConfirmation(
                email,
                LocalDateTime.now(),
                tokenSymbol,
                amount,
                sourceAddress,
                destinationAddress
        );
        walletProducer.sendTransactionConfirmation(transactionConfirmation);
    }

    private void processTokenDeposit(AccountResponse accountResponse, DepositRequest request) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.addTokens(request.tokenSymbol(), request.amount(), wallet);

        createAndSaveDepositTransaction(accountResponse.id(), wallet, request);
    }

    private void processTokenWithdrawal(AccountResponse accountResponse, WithdrawalRequest request) {
        Wallet wallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.subtractTokens(request.tokenSymbol(), request.amount(), wallet);

        createAndSaveWithdrawalTransaction(accountResponse.id(), wallet, request);

        sendTransactionConfirmation(wallet.getAddress(),accountResponse.email()
                ,request.tokenSymbol(),
                request.amount(),
                request.destinationAddress());
    }

    private void processTokenReceive(AccountResponse accountResponse, ReceiveRequest request) {
        Wallet sourceWallet = walletService.findWalletByAddress(request.sourceAddress());
        Wallet destinationWallet = walletService.findWalletByAccountId(accountResponse.id());

        tokenService.transferTokens(request.tokenSymbol(), request.amount(), sourceWallet, destinationWallet);

        createAndSaveReceiveTransaction(accountResponse.id(), sourceWallet, destinationWallet, request);
    }
    private void createAndSaveExchangeTransaction(Wallet wallet, ExchangeConfirmation confirmation) {
        ExchangeTransaction transaction = new ExchangeTransaction();
        transaction.setAccountId(confirmation.accountId());
        transaction.setTokenSymbolFrom(confirmation.symbolFrom());
        transaction.setTokenSymbolTo(confirmation.symbolTo());
        transaction.setAmountFrom(confirmation.amountFrom());
        transaction.setAmountTo(confirmation.amountTo());
        transaction.setTransactionType(TransactionType.EXCHANGE);
        transaction.setTransactionDate(confirmation.timestamp());
        transaction.setWallet(wallet);

        transactionRepository.save(transaction);
    }

    private void createAndSaveTransferTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, TransferRequest request) {
        TransferTransaction transaction = new TransferTransaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setDestinationWallet(destinationWallet);
        transaction.setTokenSymbol(request.tokenSymbol());
        transaction.setAmount(request.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.TRANSFER);

        transactionRepository.save(transaction);
    }

    private void createAndSaveDepositTransaction(Long accountId, Wallet wallet, DepositRequest request) {
        DepositTransaction transaction = new DepositTransaction();
        transaction.setAccountId(accountId);
        transaction.setDestinationWallet(wallet);
        transaction.setSourceAddress(request.sourceAddress());
        transaction.setTokenSymbol(request.tokenSymbol());
        transaction.setAmount(request.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.DEPOSIT);

        transactionRepository.save(transaction);
    }

    private void createAndSaveWithdrawalTransaction(Long accountId, Wallet wallet, WithdrawalRequest request) {
        WithdrawalTransaction transaction = new WithdrawalTransaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(wallet);
        transaction.setDestinationAddress(request.destinationAddress());
        transaction.setTokenSymbol(request.tokenSymbol());
        transaction.setAmount(request.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.WITHDRAWAL);

        transactionRepository.save(transaction);
    }

    private void createAndSaveReceiveTransaction(Long accountId, Wallet sourceWallet, Wallet destinationWallet, ReceiveRequest request) {
        ReceiveTransaction transaction = new ReceiveTransaction();
        transaction.setAccountId(accountId);
        transaction.setSourceWallet(sourceWallet);
        transaction.setDestinationWallet(destinationWallet);
        transaction.setTokenSymbol(request.tokenSymbol());
        transaction.setAmount(request.amount());
        transaction.setTransactionDate(LocalDate.now());
        transaction.setTransactionType(TransactionType.RECEIVE);

        transactionRepository.save(transaction);
    }
}
