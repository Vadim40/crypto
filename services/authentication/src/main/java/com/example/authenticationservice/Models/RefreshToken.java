package com.example.authenticationservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "refresh_token")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshToken {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn
    private Account account;
    @Column(nullable = false)
    private String tokenHash;
    @Column(nullable = false)
    private LocalDateTime expiryDate;
}
