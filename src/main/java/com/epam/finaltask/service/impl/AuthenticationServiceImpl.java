package com.epam.finaltask.service.impl;

import com.epam.finaltask.dto.AuthenticationRequest;
import com.epam.finaltask.dto.AuthenticationResponse;
import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.service.AuthenticationService;
import com.epam.finaltask.token.JwtService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

import static com.epam.finaltask.model.enums.StatusCodes.ENTITY_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final I18nUtil i18nUtil;

    @Value("${jwt.expiration}")
    private Duration expiration;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        log.info("Authenticating user with username: '{}'", request.getUsername());
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword()));

        log.info("User '{}' successfully authenticated", request.getUsername());
        return new AuthenticationResponse(jwtService.generateToken(request.getUsername()));
    }

    public void authenticateWithCookie(AuthenticationRequest request, HttpServletResponse response) {
        Cookie jwtCookie = new Cookie("jwtToken", authenticate(request).getAccessToken());
        jwtCookie.setHttpOnly(true);
        jwtCookie.setPath("/");
        jwtCookie.setMaxAge((int) expiration.toSeconds());
        response.addCookie(jwtCookie);
    }

    public boolean isCurrentUser(UUID userId) {
        String authUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        User authUser = userRepository.findByUsernameIgnoreCase(authUsername)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(),
                        i18nUtil.getMessage("error.user-not-found")));
        return authUser.getId().equals(userId);
    }
}
