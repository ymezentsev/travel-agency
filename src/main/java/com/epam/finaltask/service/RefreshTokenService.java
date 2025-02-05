package com.epam.finaltask.service;

import com.epam.finaltask.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> getRefreshToken(String token);

    RefreshToken generateRefreshToken(Long userId);

    RefreshToken verifyExpirationDate(RefreshToken token);

    int deleteRefreshToken(Long userId);
}
