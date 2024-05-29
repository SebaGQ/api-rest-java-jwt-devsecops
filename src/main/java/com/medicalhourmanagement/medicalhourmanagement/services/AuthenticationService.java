package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.RegisterRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AuthenticationRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
