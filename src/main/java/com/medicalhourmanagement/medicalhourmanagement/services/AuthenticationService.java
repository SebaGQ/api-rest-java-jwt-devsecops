package com.medicalhourmanagement.medicalhourmanagement.services;

import com.medicalhourmanagement.medicalhourmanagement.dtos.request.RegisterRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.request.AuthenticationRequestDTO;
import com.medicalhourmanagement.medicalhourmanagement.dtos.response.AuthenticationResponseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface AuthenticationService {
    AuthenticationResponseDTO register(RegisterRequestDTO request);
    AuthenticationResponseDTO authenticate(AuthenticationRequestDTO request);
    void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
