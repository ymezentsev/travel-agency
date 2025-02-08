package com.epam.finaltask.service.impl;

import com.epam.finaltask.exception.TokenExpiredException;
import com.epam.finaltask.model.RefreshToken;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.StatusCodes;
import com.epam.finaltask.repository.RefreshTokenRepository;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.service.RefreshTokenService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final I18nUtil i18nUtil;

    @Value("${jwt.refresh-expiration}")
    private Duration refreshExpiration;

    @Override
    public Optional<RefreshToken> getRefreshToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    @Transactional
    public RefreshToken generateRefreshToken(String username) {
        User user = userRepository.findByUsernameIgnoreCase(username).orElseThrow();
        deleteRefreshTokenByUser(user);

        RefreshToken refreshToken = RefreshToken.builder()
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusHours(refreshExpiration.toHours()))
                .user(user)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken verifyExpirationDate(RefreshToken token) {
        if (token.getExpiresAt().isBefore(LocalDateTime.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenExpiredException(StatusCodes.TOKEN_EXPIRED.name(),
                    i18nUtil.getMessage("error.token-expired"));
        }
        return token;
    }

    @Override
    public void deleteRefreshTokenByUser(User user) {
        if (refreshTokenRepository.existsByUser(user)) {
            refreshTokenRepository.deleteByUser(user);
        }
    }
}
