package com.hrs.core.service.jwt;

import org.springframework.security.core.userdetails.UserDetails;

public interface JwtService {

    long getExpirationTime();
    String generateToken(UserDetails userDetails);
    boolean isTokenValid(String token, UserDetails userDetails);
    String extractUsername(String token);
}
