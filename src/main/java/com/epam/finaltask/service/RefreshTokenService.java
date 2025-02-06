package com.epam.finaltask.service;

import com.epam.finaltask.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    Optional<RefreshToken> getRefreshToken(String token);

    RefreshToken generateRefreshToken(String username);

    RefreshToken verifyExpirationDate(RefreshToken token);
}
