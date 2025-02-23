package com.epam.finaltask.service;

import com.epam.finaltask.dto.AuthenticationRequest;
import com.epam.finaltask.dto.AuthenticationResponse;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.Role;
import com.epam.finaltask.service.impl.AuthenticationServiceImpl;
import com.epam.finaltask.token.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;

    @Test
    void testUserFindByUserName() {
        UUID id = UUID.fromString("05b392fe-20c2-4fad-8648-abcc19e5428c");
        String userName = "Admin";
        String password = "password";
        Role role = Role.ADMIN;

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(userName, password);

        User expectedUser = new User(
                id,
                userName,
                password,
                role,
                null,
                null,
                null,
                true,
                null
        );

        when(jwtService.generateToken(expectedUser.getUsername())).thenReturn("Token");
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ))).thenReturn(any(Authentication.class));

        AuthenticationResponse expectedResponse = new AuthenticationResponse("Token", "");
        AuthenticationResponse actualResponse = authenticationService.authenticate(authenticationRequest);

        assertEquals(expectedResponse, actualResponse);
    }
}
