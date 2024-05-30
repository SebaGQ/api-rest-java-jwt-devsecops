package com.medicalhourmanagement.medicalhourmanagement.services.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicalhourmanagement.medicalhourmanagement.entities.Patient;
import com.medicalhourmanagement.medicalhourmanagement.enums.Role;
import com.medicalhourmanagement.medicalhourmanagement.enums.TokenType;
import com.medicalhourmanagement.medicalhourmanagement.entities.Token;
import com.medicalhourmanagement.medicalhourmanagement.auth.services.JwtService;
import com.medicalhourmanagement.medicalhourmanagement.dtos.RegisterRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AuthenticationRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AuthenticationResponseDTO;
import com.medicalhourmanagement.medicalhourmanagement.repositories.TokenRepository;
import com.medicalhourmanagement.medicalhourmanagement.repositories.PatientRepository;
import com.medicalhourmanagement.medicalhourmanagement.services.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private static final String BEARER_PREFIX = "Bearer ";

  private final PatientRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  @Override
  public AuthenticationResponseDTO register(RegisterRequestDTO request) {
    Patient user = buildPatientFromRequest(request);
    Patient savedUser = repository.save(user);
    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return buildAuthResponse(jwtToken, refreshToken);
  }

  @Override
  public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request) {
    Patient user = repository.findByEmail(request.getEmail())
            .orElseThrow(() -> new BadCredentialsException("User not found"));

    authenticateUser(request.getEmail(), request.getPassword());

    String jwtToken = jwtService.generateToken(user);
    String refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return buildAuthResponse(jwtToken, refreshToken);
  }

  @Override
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (isInvalidAuthHeader(authHeader)) {
      return;
    }

    String refreshToken = extractToken(authHeader);
    String userEmail = jwtService.extractUsername(refreshToken);

    if (userEmail != null) {
      processRefreshToken(response, refreshToken, userEmail);
    }
  }

  private boolean isInvalidAuthHeader(String authHeader) {
    return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
  }

  private String extractToken(String authHeader) {
    return authHeader.substring(BEARER_PREFIX.length());
  }

  private void processRefreshToken(HttpServletResponse response, String refreshToken, String userEmail) throws IOException {
    Patient user = repository.findByEmail(userEmail).orElseThrow();
    if (jwtService.isTokenValid(refreshToken, user)) {
      String accessToken = jwtService.generateToken(user);
      revokeAllUserTokens(user);
      saveUserToken(user, accessToken);
      writeAuthResponse(response, buildAuthResponse(accessToken, refreshToken));
    }
  }

  private void writeAuthResponse(HttpServletResponse response, AuthenticationResponseDTO authResponse) throws IOException {
    response.setContentType("application/json");
    new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
  }

  private Patient buildPatientFromRequest(RegisterRequestDTO request) {
    return Patient.builder()
            .firstName(request.getFirstname())
            .lastName(request.getLastname())
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .role(Role.USER)
            .build();
  }

  private void authenticateUser(String email, String password) {
    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
    );
  }

  private AuthenticationResponseDTO buildAuthResponse(String jwtToken, String refreshToken) {
    return AuthenticationResponseDTO.builder()
            .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .build();
  }

  private void saveUserToken(Patient patient, String jwtToken) {
    Token token = Token.builder()
            .patient(patient)
            .accessToken(jwtToken)
            .tokenType(TokenType.BEARER)
            .expired(false)
            .revoked(false)
            .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(Patient patient) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(patient.getId());
    if (validUserTokens.isEmpty()) {
      return;
    }
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }
}
