package com.medicalhourmanagement.medicalhourmanagement.security.filters;

import com.medicalhourmanagement.medicalhourmanagement.security.services.JwtService;
import com.medicalhourmanagement.medicalhourmanagement.utils.constants.AuthConstants;
import com.medicalhourmanagement.medicalhourmanagement.utils.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.ExpiredTokenException;
import com.medicalhourmanagement.medicalhourmanagement.exceptions.dtos.InvalidTokenException;
import com.medicalhourmanagement.medicalhourmanagement.repositories.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);

  private final JwtService jwtService;
  private final UserDetailsService userDetailsService;
  private final TokenRepository tokenRepository;

  @Override
  protected void doFilterInternal(
          @NonNull HttpServletRequest request,
          @NonNull HttpServletResponse response,
          @NonNull FilterChain filterChain
  ) throws ServletException, IOException {
    logger.debug("Processing request to: " + request.getServletPath());

    if (isAuthPath(request)) {
      logger.debug("Skipping JWT authentication for auth path");
      filterChain.doFilter(request, response);
      return;
    }

    final String authHeader = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
    if (isInvalidAuthHeader(authHeader)) {
      logger.debug("Invalid auth header, proceeding with filter chain");
      filterChain.doFilter(request, response);
      return;
    }

    final String jwt = extractJwtFromHeader(authHeader);
    final String userEmail;

    try {
      userEmail = jwtService.extractUsername(jwt);
    } catch (ExpiredTokenException e) {
      logger.warn("Expired token: {}", e.getMessage());
      handleException(response, e.getMessage(), HttpServletResponse.SC_UNAUTHORIZED);
      return;
    } catch (InvalidTokenException e) {
      logger.warn("Invalid token: {}", e.getMessage());
      handleException(response, e.getMessage(), HttpServletResponse.SC_BAD_REQUEST);
      return;
    }

    if (userEmail != null && isNotAuthenticated()) {
      processTokenAuthentication(request, jwt, userEmail);
    }

    filterChain.doFilter(request, response);
  }

  private boolean isAuthPath(HttpServletRequest request) {
    return request.getServletPath().equals(EndpointsConstants.ENDPOINT_AUTH + "/login") ||
            request.getServletPath().equals(EndpointsConstants.ENDPOINT_AUTH + "/register");
  }

  private boolean isInvalidAuthHeader(String authHeader) {
    return authHeader == null || !authHeader.startsWith(AuthConstants.BEARER_PREFIX);
  }

  private String extractJwtFromHeader(String authHeader) {
    return authHeader.substring(AuthConstants.BEARER_PREFIX.length());
  }

  private boolean isNotAuthenticated() {
    return SecurityContextHolder.getContext().getAuthentication() == null;
  }

  private void processTokenAuthentication(HttpServletRequest request, String jwt, String userEmail) {
    UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);
    boolean isTokenValid = tokenRepository.findByAccessToken(jwt)
            .map(t -> !t.isExpired() && !t.isRevoked())
            .orElse(false);

    logger.debug("Processing token authentication for user: " + userEmail);
    logger.debug("Is token valid: " + isTokenValid);

    if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
              userDetails, null, userDetails.getAuthorities()
      );
      authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
      SecurityContextHolder.getContext().setAuthentication(authToken);
      logger.debug("Authentication set in SecurityContext for user: " + userEmail);
    } else {
      logger.debug("Token validation failed for user: " + userEmail);
    }
  }

  private void handleException(HttpServletResponse response, String message, int status) throws IOException {
    response.setStatus(status);
    response.setContentType(AuthConstants.CONTENT_TYPE_JSON);
    response.getWriter().write("{\"error\": \"" + message + "\"}");
    response.getWriter().flush();
  }
}