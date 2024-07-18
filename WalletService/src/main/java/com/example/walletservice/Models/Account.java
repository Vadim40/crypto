package com.example.walletservice.Models;

import com.example.walletservice.Models.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;


import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class Account {
    @Id
    private String id;

    private String password;
    @Indexed(unique = true)
    private String email;
    private Map<String, Double> crypto ;
    private List<String> transactionsId;
    private List<Role> roles;

    public Account(String password,
                   String email,
                   Map<String, Double> crypto) {
        this.password = password;
        this.email = email;
        this.crypto = crypto;
    }
}
