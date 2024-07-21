package com.example.authenticationservice.Models;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {
    private final boolean isOTPEnabled;

    public CustomUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities, boolean isOTPEnabled) {
        super(username, password, authorities);
        this.isOTPEnabled = isOTPEnabled;
    }

    public boolean isOTPEnabled() {
        return isOTPEnabled;
    }
}
