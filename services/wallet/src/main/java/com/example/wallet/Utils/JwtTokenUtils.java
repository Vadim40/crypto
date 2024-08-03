package com.example.wallet.Utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtTokenUtils {
    @Value("${jwt.secret}")
    private  String secret;


    public String getUsername(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getAllClaimsFromToken(token).get("roles", List.class);
    }
    public boolean isOtpToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return claims.get("otp", Boolean.class) != null && claims.get("otp", Boolean.class);
    }



    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

}