package com.example.wallet.Controllers;

import com.example.wallet.Mappers.WalletMapper;
import com.example.wallet.Models.DTO.WalletDTO;
import com.example.wallet.Models.Wallet;
import com.example.wallet.Services.Interfaces.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/wallet")
@RequiredArgsConstructor
public class WalletController {
    private final WalletService walletService;
    private final WalletMapper walletMapper;
    @GetMapping("/address/{address}")
    public ResponseEntity<WalletDTO> findWalletByAddress(@PathVariable("address") String address){
        Wallet wallet=walletService.findWalletByAddress(address);
        WalletDTO walletDTO=walletMapper.mapWalletToWalletDTO(wallet);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }
    @GetMapping("/id/{id}")
    public ResponseEntity<WalletDTO> findWalletById(@PathVariable("id") Long id){
        Wallet wallet=walletService.findWalletById(id);
        WalletDTO walletDTO=walletMapper.mapWalletToWalletDTO(wallet);
        return new ResponseEntity<>(walletDTO, HttpStatus.OK);
    }
}
