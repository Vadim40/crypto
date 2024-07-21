package com.example.authenticationservice.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Otp {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn
    private Account account;
    @Column
    private String otp;


}
