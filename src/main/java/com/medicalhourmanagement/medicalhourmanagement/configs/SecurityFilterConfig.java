package com.medicalhourmanagement.medicalhourmanagement.configs;

import com.medicalhourmanagement.medicalhourmanagement.security.filters.JwtAuthFilter;
import com.medicalhourmanagement.medicalhourmanagement.constants.EndpointsConstants;
import com.medicalhourmanagement.medicalhourmanagement.constants.RoleConstants;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String[] WHITE_LIST_URL = {
            EndpointsConstants.ENDPOINT_AUTH_PATTERN,
            EndpointsConstants.ENDPOINT_ACTUATOR_PATTERN
    };
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(GET, EndpointsConstants.ENDPOINT_DOCTORS_PATTERN).authenticated()
                                .requestMatchers(POST, EndpointsConstants.ENDPOINT_DOCTORS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)
                                .requestMatchers(PUT, EndpointsConstants.ENDPOINT_DOCTORS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)
                                .requestMatchers(DELETE, EndpointsConstants.ENDPOINT_DOCTORS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)

                                .requestMatchers(GET, EndpointsConstants.ENDPOINT_PATIENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN, RoleConstants.ROLE_MANAGER)
                                .requestMatchers(POST, EndpointsConstants.ENDPOINT_PATIENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)
                                .requestMatchers(PUT, EndpointsConstants.ENDPOINT_PATIENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)
                                .requestMatchers(DELETE, EndpointsConstants.ENDPOINT_PATIENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_ADMIN)

                                .requestMatchers(GET, EndpointsConstants.ENDPOINT_APPOINTMENTS_PATTERN).authenticated()
                                .requestMatchers(POST, EndpointsConstants.ENDPOINT_APPOINTMENTS_PATTERN).authenticated()
                                .requestMatchers(PUT, EndpointsConstants.ENDPOINT_APPOINTMENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_MANAGER, RoleConstants.ROLE_ADMIN)
                                .requestMatchers(DELETE, EndpointsConstants.ENDPOINT_APPOINTMENTS_PATTERN).hasAnyRole(RoleConstants.ROLE_MANAGER, RoleConstants.ROLE_ADMIN)
                                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(EndpointsConstants.ENDPOINT_LOGOUT)
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );
        return http.build();
    }
}
