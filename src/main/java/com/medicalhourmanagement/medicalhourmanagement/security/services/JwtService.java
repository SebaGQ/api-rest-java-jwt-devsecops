package com.medicalhourmanagement.medicalhourmanagement.security.services;

import com.medicalhourmanagement.medicalhourmanagement.constants.AuthConstants;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.ExpiredTokenException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import com.medicalhourmanagement.medicalhourmanagement.constants.ExceptionMessageConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

  private static final Logger LOGGER = LoggerFactory.getLogger(JwtService.class);

  @Value("${application.security.jwt.secret-key}")
  private String secretKey;
  @Value("${application.security.jwt.expiration}")
  private long jwtExpiration;
  @Value("${application.security.jwt.refresh-token.expiration}")
  private long refreshExpiration;

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  public String generateToken(UserDetails userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put(AuthConstants.ROLE_CLAIM, userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .findFirst().orElseThrow(() -> {
              LOGGER.error(ExceptionMessageConstants.ROLE_NOT_FOUND_MSG);
              return new RuntimeException(ExceptionMessageConstants.ROLE_NOT_FOUND_MSG);
            }));

    return generateToken(claims, userDetails);
  }

  public String generateToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails
  ) {
    return buildToken(extraClaims, userDetails, jwtExpiration);
  }

  public String generateRefreshToken(
          UserDetails userDetails
  ) {
    return buildToken(new HashMap<>(), userDetails, refreshExpiration);
  }

  private String buildToken(
          Map<String, Object> extraClaims,
          UserDetails userDetails,
          long expiration
  ) {
    LOGGER.info("Building token for user {}", userDetails.getUsername());
    return Jwts
            .builder()
            .setClaims(extraClaims)
            .setSubject(userDetails.getUsername())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(getSignInKey(), SignatureAlgorithm.HS512)
            .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String username = extractUsername(token);
    boolean isValid = (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    LOGGER.info("Token validation for user {}: {}", username, isValid);
    return isValid;
  }

  private boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  private Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  private Claims extractAllClaims(String token) {
    try {
      return Jwts
              .parserBuilder()
              .setSigningKey(getSignInKey())
              .build()
              .parseClaimsJws(token)
              .getBody();
    } catch (ExpiredJwtException e) {
      LOGGER.warn("Expired token: {}", e.getMessage());
      throw new ExpiredTokenException(ExceptionMessageConstants.EXPIRED_TOKEN_MSG);
    } catch (IllegalArgumentException e) {
      LOGGER.warn("Invalid token: {}", e.getMessage());
      throw new InvalidTokenException(ExceptionMessageConstants.INVALID_TOKEN_MSG);
    }
  }

  private Key getSignInKey() {
    byte[] keyBytes = Decoders.BASE64.decode(secretKey);
    return Keys.hmacShaKeyFor(keyBytes);
  }
}
