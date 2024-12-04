package com.epam.finaltask.service;

import com.epam.finaltask.auth.AuthenticationRequest;
import com.epam.finaltask.auth.AuthenticationResponse;
import com.epam.finaltask.auth.AuthenticationService;
import com.epam.finaltask.config.JwtService;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.Role;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource
public class AuthenticationServiceTest {
    @InjectMocks
    private AuthenticationService authenticationService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtService jwtService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private PasswordEncoder passwordEncoder;

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
                true
        );

        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.of(expectedUser));
        when(jwtService.generateToken(expectedUser)).thenReturn("Token");
        when(passwordEncoder.matches(password, password)).thenReturn(true);
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),
                authenticationRequest.getPassword()
        ))).thenReturn(any(Authentication.class));

        AuthenticationResponse expectedResponse = new AuthenticationResponse("Token");
        AuthenticationResponse actualResponse = authenticationService.authenticate(authenticationRequest);

        assertEquals(expectedResponse, actualResponse);
        verify(userRepository, times(1)).findUserByUsername(userName);

    }

    @Test
    void testCheckLoginData_IrrelevantUserName() {

        String userName = "Robin";
        String password = "password";

        AuthenticationRequest request = new AuthenticationRequest(userName, password);

        when(userRepository.findUserByUsername(userName)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));
    }

    @Test
    void testCheckLoginData_InvalidPassword() {

        AuthenticationRequest request = new AuthenticationRequest("Admin", "cookie");

        when(userRepository.findUserByUsername("Admin")).thenReturn(Optional.of(new User()));
        assertThrows(EntityNotFoundException.class, () -> authenticationService.authenticate(request));
    }

}
