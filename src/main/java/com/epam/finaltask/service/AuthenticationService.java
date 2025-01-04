package com.epam.finaltask.service;

import com.epam.finaltask.dto.AuthenticationRequest;
import com.epam.finaltask.dto.AuthenticationResponse;
import jakarta.servlet.http.HttpServletResponse;

import java.util.UUID;

public interface AuthenticationService {
    AuthenticationResponse authenticate(AuthenticationRequest request);

    void authenticateWithCookie(AuthenticationRequest request, HttpServletResponse response);

    boolean isCurrentUser(UUID userId);
}
