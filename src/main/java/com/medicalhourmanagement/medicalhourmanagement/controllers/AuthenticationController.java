package com.medicalhourmanagement.medicalhourmanagement.controllers;

import com.medicalhourmanagement.medicalhourmanagement.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.AuthenticationRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.response.AuthenticationResponseDTO;
import com.medicalhourmanagement.medicalhourmanagement.services.impl.AuthenticationServiceImpl;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.RegisterRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping(EndpointsConstants.ENDPOINT_AUTH)
@RequiredArgsConstructor
public class AuthenticationController {

  private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);
  private final AuthenticationServiceImpl service;

  @PostMapping("/register")
  public ResponseEntity<AuthenticationResponseDTO> register(@RequestBody @Valid RegisterRequestDTO request) {
    LOGGER.info("Received request to register a new user with email: {}", request.getEmail());
    AuthenticationResponseDTO response = service.register(request);
    LOGGER.info("User registered successfully with email: {}", request.getEmail());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/authenticate")
  public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO request) {
    LOGGER.info("Received request to authenticate user with email: {}", request.getEmail());
    AuthenticationResponseDTO response = service.authenticate(request);
    LOGGER.info("User authenticated successfully with email: {}", request.getEmail());
    return ResponseEntity.ok(response);
  }

  @PostMapping("/refresh-token")
  public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
    LOGGER.info("Received request to refresh token");
    service.refreshToken(request, response);
    LOGGER.info("Token refreshed successfully");
  }
}
