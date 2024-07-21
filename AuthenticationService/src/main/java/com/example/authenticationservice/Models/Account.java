package com.example.authenticationservice.Models;

import com.example.authenticationservice.Models.Enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String password;
    @Column
    private String email;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "account_role",
            joinColumns = @JoinColumn(name = "account_id")
    )
    @Column(name = "role")
    private List<Role> roles = new ArrayList<>();
    @OneToOne(mappedBy = "account")
    private Otp otp =new Otp();

    @Column
    private boolean isOTPEnabled ;
}
