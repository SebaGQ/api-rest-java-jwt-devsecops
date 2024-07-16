package com.medicalhourmanagement.medicalhourmanagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.AuthenticationRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.RegisterRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.response.AuthenticationResponseDTO;
import com.medicalhourmanagement.medicalhourmanagement.security.services.JwtService;
import com.medicalhourmanagement.medicalhourmanagement.services.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationServiceImpl authenticationService;

    @Autowired
    private ObjectMapper objectMapper;

    private RegisterRequestDTO registerRequest;
    private AuthenticationRequestDTO authRequest;
    private AuthenticationResponseDTO authResponse;

    @Autowired
    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        registerRequest = RegisterRequestDTO.builder()
                .firstname("John")
                .lastname("Doe")
                .email("john.doe@example.com")
                .password("Password123")
                .build();

        authRequest = AuthenticationRequestDTO.builder()
                .email("john.doe@example.com")
                .password("Password123")
                .build();

        authResponse = AuthenticationResponseDTO.builder()
                .accessToken("access-token")
                .refreshToken("refresh-token")
                .build();
    }

    @Test
    public void testRegister() throws Exception {
        Mockito.when(authenticationService.register(Mockito.any(RegisterRequestDTO.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authResponse)));
    }

    @Test
    public void testAuthenticate() throws Exception {
        Mockito.when(authenticationService.authenticate(Mockito.any(AuthenticationRequestDTO.class)))
                .thenReturn(authResponse);

        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authRequest)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(authResponse)));
    }

    @Test
    @WithMockUser
    public void testRefreshToken() throws Exception {
        // Suponiendo que JwtService tiene un método generateRefreshToken que acepta un UserDetails
        UserDetails userDetails = Mockito.mock(UserDetails.class);
        Mockito.when(userDetails.getUsername()).thenReturn("john.doe@example.com");

        String refreshToken = jwtService.generateRefreshToken(userDetails);

        Mockito.doNothing().when(authenticationService).refreshToken(Mockito.any(), Mockito.any());

        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + refreshToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}