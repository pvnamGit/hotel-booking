package com.hrs.infrastructure.jwt;

import com.hrs.core.service.jwt.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtServiceImpl implements JwtService {
  @Value("${security.jwt.secret_key}")
  private String secretKey;

  @Value("${security.jwt.expiration}")
  private long jwtExpiration;

  @Override
  public long getExpirationTime() {
    return jwtExpiration;
  }

  private String buildToken(
      Map<String, Object> extractClaims, UserDetails userDetails, long jwtExpiration) {
    var currentTime = new Date(System.currentTimeMillis());
    var expireTime = new Date(System.currentTimeMillis() + jwtExpiration);
    return Jwts.builder()
        .setClaims(extractClaims)
        .setSubject(userDetails.getUsername())
        .setIssuedAt(currentTime)
        .setExpiration(expireTime)
        .signWith(SignatureAlgorithm.HS256, getSignInKey())
        .compact();
  }

  public String generateToken(Map<String, Object> extractClaims, UserDetails userDetails) {
    return buildToken(extractClaims, userDetails, jwtExpiration);
  }

  @Override
  public String generateToken(UserDetails userDetails) {
    return generateToken(new HashMap<>(), userDetails);
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  // ============ validate token =============================

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  @Override
  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
  }

  // ========== Extract account ================
  private Claims extractAllClaims(String token) {
    return Jwts.parser().setSigningKey(getSignInKey()).parseClaimsJws(token).getBody();
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  @Override
  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }
}
