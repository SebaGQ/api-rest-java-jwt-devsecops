package com.medicalhourmanagement.medicalhourmanagement.auth.filters;

import com.medicalhourmanagement.medicalhourmanagement.auth.services.JwtService;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.ExpiredTokenException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InvalidTokenException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_PREFIX = "Bearer ";
  private static final String ENDPOINT_AUTH = "/api/v1/auth";

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    if (isAuthPath(request)) {
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(AUTHORIZATION_HEADER);
    if (isInvalidAuthHeader(authHeader)) {
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = extractJwtFromHeader(authHeader);
    final String userEmail;

    try {
      userEmail = jwtService.extractUsername(jwt);
    } catch (ExpiredTokenException e) {
      handleException(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } catch (InvalidTokenException e) {
      handleException(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    if (userEmail != null && isNotAuthenticated()) {
      processTokenAuthentication(request, jwt, userEmail);
    }

    filterChain.doFilter(request, response);
  }

  private boolean isAuthPath(HttpServletRequest request) {
    return request.getServletPath().contains(ENDPOINT_AUTH);
  }

  private boolean isInvalidAuthHeader(String authHeader) {
    return authHeader == null || !authHeader.startsWith(BEARER_PREFIX);
  }

  private String extractJwtFromHeader(String authHeader) {
    return authHeader.substring(BEARER_PREFIX.length());
  }

  private boolean isNotAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication() == null;
  }

  private void processTokenAuthentication(HttpServletRequest request, String jwt, String userEmail) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    boolean isTokenValid = tokenRepository.findByAccessToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);

    if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
      );
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
    }
  }

  private void handleException(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json");
    response.getWriter().write("{\"error\": \"" + message + "\"}");
    response.getWriter().flush();
  }
}
