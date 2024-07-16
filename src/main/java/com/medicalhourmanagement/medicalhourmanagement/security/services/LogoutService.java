package com.medicalhourmanagement.medicalhourmanagement.security.services;

import com.medicalhourmanagement.medicalhourmanagement.utils.constants.AuthConstants;
import com.medicalhourmanagement.medicalhourmanagement.repositories.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

  private final TokenRepository tokenRepository;

  @Override
  public void logout(
          HttpServletRequest request,
          HttpServletResponse response,
          Authentication authentication
  ) {
    final String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
    final String jwt;
    if (authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX)) {
      return;
    }
    jwt = authHeader.substring(AuthConstants.BEARER_PREFIX.length());
    var storedToken = tokenRepository.findByAccessToken(jwt)
            .orElse(null);
    if (storedToken != null) {
      storedToken.setExpired(true);
      storedToken.setRevoked(true);
      tokenRepository.save(storedToken);
      SecurityContextHolder.clearContext();
    }
  }
}
