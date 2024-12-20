package com.epam.finaltask.auth;

import com.epam.finaltask.token.JwtService;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;
import static com.epam.finaltask.exception.StatusCodes.INVALID_CREDENTIALS;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${jwt.expiration}")
    private Duration expiration;

    //todo add update username to lower case
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//        request.setUsername(request.getUsername().toLowerCase().strip());
        User user = userRepository.findUserByUsername(request.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException(INVALID_CREDENTIALS.name(), "Invalid credentials");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        return new AuthenticationResponse(jwtService.generateToken(user));
    }

    public void authenticateWithCookie(AuthenticationRequest request, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", authenticate(request).getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) expiration.toSeconds());
        response.addCookie(jwtCookie);
    }
}
