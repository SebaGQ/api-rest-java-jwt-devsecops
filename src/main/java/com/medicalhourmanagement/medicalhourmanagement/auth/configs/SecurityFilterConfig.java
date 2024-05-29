package com.medicalhourmanagement.medicalhourmanagement.auth.configs;

import com.medicalhourmanagement.medicalhourmanagement.auth.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.medicalhourmanagement.medicalhourmanagement.enums.Role.ADMIN;
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

    private static final String ENDPOINT_AUTH = "api/v1/auth/**";
    private static final String ENDPOINT_DOCTORS = "api/v1/doctors/**";

    private static final String[] WHITE_LIST_URL = {ENDPOINT_AUTH};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(req ->
                        //Importante especificar cuáles endpoints NO requieren auth, no al reves
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll()
                                .requestMatchers(GET, ENDPOINT_DOCTORS).permitAll()
                                .requestMatchers( PUT, ENDPOINT_DOCTORS).hasRole(ADMIN.name())
                                .requestMatchers( POST, ENDPOINT_DOCTORS).hasRole(ADMIN.name())

                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl(ENDPOINT_AUTH+"/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                )
        ;
        return http.build();
    }
}