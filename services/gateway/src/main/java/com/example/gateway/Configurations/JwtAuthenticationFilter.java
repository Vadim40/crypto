package com.example.gateway.Configurations;

import com.example.gateway.DTO.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {

    private final String secret = "2$2a$12$uHQpJLE9OUEEqeKQg48CYOLC";

    public static class Config {
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                return chain.filter(exchange);
            }

            String jwt = authHeader.substring(7);

            try {
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(jwt)
                        .getBody();

                Date expiration = claims.getExpiration();
                if (expiration.before(Date.from(Instant.now()))) {
                    return handleUnauthorized(exchange, "JWT token has expired");
                }

                Boolean isOtpToken = claims.get("otp", Boolean.class);
                String username = claims.getSubject();
                List<String> roles = claims.get("roles", List.class);
                String rolesHeader = String.join(",", roles);

                ServerWebExchange mutatedExchange = exchange.mutate()
                        .request(exchange.getRequest().mutate()
                                .header("X-User-Name", username)
                                .header("X-User-Roles", rolesHeader)
                                .build())
                        .build();

                if (exchange.getRequest().getURI().getPath().equals("/api/v1/auth/verify-otp")) {
                    if (Boolean.TRUE.equals(isOtpToken)) {
                        return chain.filter(mutatedExchange);
                    } else {
                        return handleForbidden(mutatedExchange, "Final token cannot be used for OTP verification");
                    }
                } else {
                    if (Boolean.TRUE.equals(isOtpToken)) {
                        return handleForbidden(mutatedExchange, "OTP token can only be used for OTP verification");
                    }
                }

                return chain.filter(mutatedExchange);

            } catch (ExpiredJwtException e) {
                return handleUnauthorized(exchange, e.getMessage());
            } catch (SignatureException e) {
                return handleUnauthorized(exchange, "Invalid JWT signature");
            } catch (Exception e) {
                return handleUnauthorized(exchange, "Invalid JWT token");
            }
        };
    }

    private Mono<Void> handleUnauthorized(ServerWebExchange exchange, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Unauthorized",
                message,
                HttpStatus.UNAUTHORIZED.value(),
                exchange.getRequest().getURI().getPath()
        );

        return writeErrorResponse(exchange, errorResponse, HttpStatus.UNAUTHORIZED);
    }

    private Mono<Void> handleForbidden(ServerWebExchange exchange, String message) {
        ErrorResponse errorResponse = new ErrorResponse(
                "Forbidden",
                message,
                HttpStatus.FORBIDDEN.value(),
                exchange.getRequest().getURI().getPath()
        );

        return writeErrorResponse(exchange, errorResponse, HttpStatus.FORBIDDEN);
    }

    private Mono<Void> writeErrorResponse(ServerWebExchange exchange, ErrorResponse errorResponse, HttpStatus status) {
        try {
            exchange.getResponse().setStatusCode(status);
            exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
            byte[] bytes = new ObjectMapper().writeValueAsBytes(errorResponse);
            return exchange.getResponse().writeWith(Mono.just(exchange.getResponse().bufferFactory().wrap(bytes)));
        } catch (Exception e) {
            return Mono.error(e);
        }
    }

}
