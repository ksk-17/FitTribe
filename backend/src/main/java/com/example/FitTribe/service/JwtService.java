package com.example.FitTribe.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private static final String SECRET_KEY =
      "tbljaLMd8PXakXtfJK2dvuUVp0OBqynCB3Bdr3nY0x65xq8bDH5WQUAESIgsdOuXysPl+xDtVEh3ZcZyk6ZZ6EgAJcsUJA1k7ZVY09/o4ao=";

  public String extractUserName(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = getAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims getAllClaims(String token) {
    return Jwts.parser().verifyWith(getSigningKey()).build().parseSignedClaims(token).getPayload();
  }

  public boolean isTokenValid(String userName, String token) {
    return userName.equals(extractUserName(token)) && !isTokenExpired(token);
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String getToken(String userName) {
    Map<String, Object> claims = new HashMap<>();
    return generateToken(claims, userName);
  }

  private String generateToken(Map<String, Object> claims, String userName) {
    return Jwts.builder()
        .claims(claims)
        .subject(userName)
        .issuedAt(new Date(System.currentTimeMillis()))
        .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
        .signWith(getSigningKey(), SignatureAlgorithm.HS256)
        .compact();
  }

  private SecretKey getSigningKey() {
    byte[] key = Decoders.BASE64.decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(key);
  }
}
