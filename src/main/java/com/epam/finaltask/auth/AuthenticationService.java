package com.epam.finaltask.auth;

import com.epam.finaltask.config.JwtService;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;
import static com.epam.finaltask.exception.StatusCodes.INVALID_CREDENTIALS;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        User user = userRepository.findUserByUsername(authenticationRequest.getUsername())
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), "Can't find user by username"));

        if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException(INVALID_CREDENTIALS.name(), "Invalid credentials");
        }

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));

        return new AuthenticationResponse(jwtService.generateToken(user));
    }
}
