package com.example.authenticationservice.Models;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class OTP {
    @Id
    private Long id;

    @OneToOne
    @MapsId
    @JoinColumn
    private Account account;



}
