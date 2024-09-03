package com.example.wallet.Controllers;

import com.example.wallet.Mappers.TransactionMapper;
import com.example.wallet.Models.DTO.*;
import com.example.wallet.Models.Transaction;
import com.example.wallet.Services.Interfaces.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final TransactionMapper transactionMapper;

    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> findTransactionById(@PathVariable("id") Long id) {
        Transaction transaction = transactionService.findTransactionById(id);
        TransactionDTO transactionDTO = transactionMapper.mapTransactionToTransactionDTO(transaction);
        return new ResponseEntity<>(transactionDTO, HttpStatus.OK);
    }


    @GetMapping("/transactions")
    public ResponseEntity<List<TransactionDTO>> findTransactionsByTypeAfterDate(
            @RequestBody @Valid TransactionFilterRequest request,
            @RequestHeader(value = "X-User-Name") String email) {
        LocalDate date = getDefaultDate(request.date());
        List<Transaction> transactions = transactionService
                .findAccountTransactionsByTransactionTypeAfterDate(request.transactionType(), date, email);
        List<TransactionDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::mapTransactionToTransactionDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(transactionDTOs, HttpStatus.OK);
    }

    private LocalDate getDefaultDate(LocalDate date) {
        return date != null ? date : LocalDate.now().minusMonths(1);
    }

    @PostMapping("/transfer-tokens")
    public ResponseEntity<Object> transferTokens(
            @RequestBody @Valid TransferRequest request,
            @RequestHeader(value = "X-User-Name") String email) {
        transactionService.transferTokens(request, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/deposit-tokens")
    public ResponseEntity<Object> depositTokens(
            @RequestBody @Valid DepositRequest request,
            @RequestHeader(value = "X-User-Name") String email) {
        transactionService.depositTokens(request, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdrawal-tokens")
    public ResponseEntity<Object> withdrawalTokens(
            @RequestBody @Valid WithdrawalRequest request,
            @RequestHeader(value = "X-User-Name") String email) {
        transactionService.withdrawTokens(request, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/receive-tokens")
    public ResponseEntity<Object> receiveTokens(
            @RequestBody @Valid ReceiveRequest request,
            @RequestHeader(value = "X-User-Name") String email) {
        transactionService.receiveTokens(request, email);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
