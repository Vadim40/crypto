package com.example.authenticationservice.Models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table
@AllArgsConstructor
@NoArgsConstructor
public class Otp {
    @Id
    private Long id;
    @OneToOne
    @MapsId
    @JoinColumn
    private Account account;
    @Column(nullable = false)
    private String otp;

    @Column(nullable = false)
    private LocalDateTime expiryTime;
}
