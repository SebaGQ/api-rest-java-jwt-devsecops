package com.medicalhourmanagement.medicalhourmanagement.auth.configs;

import com.medicalhourmanagement.medicalhourmanagement.auth.filters.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.cors.CorsConfigurationSource;

import static com.medicalhourmanagement.medicalhourmanagement.enums.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityFilterConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;
    private final CorsConfigurationSource corsConfigurationSource;

    private static final String ENDPOINT_AUTH = "api/v1/auth/**";
    private static final String ENDPOINT_ACTUATOR = "actuator/**";
    private static final String ENDPOINT_DOCTORS = "api/v1/doctors/**";
    private static final String ENDPOINT_PATIENTS = "api/v1/patients/**";
    private static final String ENDPOINT_APPOINTMENTS = "api/v1/appointments/**";

    private static final String[] WHITE_LIST_URL = {ENDPOINT_AUTH, ENDPOINT_ACTUATOR};
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource))
                .csrf(Customizer.withDefaults())
                .authorizeHttpRequests(req ->
                        //Recomendable comenzar especificando cuáles endpoints NO requieren auth, y terminar con un  .anyRequest().authenticated()
                        req
                                .requestMatchers(WHITE_LIST_URL).permitAll()

                                .requestMatchers(GET, ENDPOINT_DOCTORS).authenticated()
                                .requestMatchers(POST, ENDPOINT_DOCTORS).hasRole(ADMIN.name())
                                .requestMatchers(PUT, ENDPOINT_DOCTORS).hasRole(ADMIN.name())
                                .requestMatchers(DELETE, ENDPOINT_DOCTORS).hasRole(ADMIN.name())

                                .requestMatchers(GET, ENDPOINT_PATIENTS).authenticated()
                                .requestMatchers(POST, ENDPOINT_PATIENTS).hasRole(ADMIN.name())
                                .requestMatchers(PUT, ENDPOINT_PATIENTS).hasRole(ADMIN.name())
                                .requestMatchers(DELETE, ENDPOINT_PATIENTS).hasRole(ADMIN.name())

                                .requestMatchers(GET, ENDPOINT_APPOINTMENTS).authenticated()
                                //Se debe permitir que el usuario registre citas, solo se debe validar que si el rol es USER, el rut de la cita registrada solo puede ser propio
                                .requestMatchers(POST, ENDPOINT_APPOINTMENTS).authenticated()
                                .requestMatchers(PUT, ENDPOINT_APPOINTMENTS).hasRole(MANAGER.name())
                                .requestMatchers(DELETE, ENDPOINT_APPOINTMENTS).hasRole(MANAGER.name())

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