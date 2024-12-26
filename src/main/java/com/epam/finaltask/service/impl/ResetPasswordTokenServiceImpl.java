package com.epam.finaltask.service.impl;

import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.exception.StatusCodes;
import com.epam.finaltask.exception.TokenAlreadyConfirmedException;
import com.epam.finaltask.exception.TokenExpiredException;
import com.epam.finaltask.model.ResetPasswordToken;
import com.epam.finaltask.model.User;
import com.epam.finaltask.repository.ResetPasswordTokenRepository;
import com.epam.finaltask.service.ResetPasswordTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;
import static com.epam.finaltask.exception.StatusCodes.TOKEN_ALREADY_USED;

@Slf4j
@Service
@RequiredArgsConstructor
public class ResetPasswordTokenServiceImpl implements ResetPasswordTokenService {
    private final ResetPasswordTokenRepository resetPasswordTokenRepository;

    private static final String TOKEN_NOT_FOUND = "Token not found";
    private static final String TOKEN_CONFIRMED = "Token has already been used";
    private static final String TOKEN_EXPIRED = "Token has expired";

    @Value("${reset.password.token.lifetime}")
    private Long tokenLifetime;

    @Override
    public ResetPasswordToken generateResetPasswordToken(User user) {
        return ResetPasswordToken.builder()
                .token(UUID.randomUUID().toString())
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(tokenLifetime))
                .user(user)
                .build();
    }

    @Override
    public String saveResetPasswordToken(ResetPasswordToken token) {
        return resetPasswordTokenRepository.save(token).getToken();
    }

    @Override
    public ResetPasswordToken getResetPasswordToken(String token) {
        return resetPasswordTokenRepository.findByToken(token)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), TOKEN_NOT_FOUND));
    }

    @Override
    public void validateResetPasswordToken(ResetPasswordToken resetPasswordToken) {
        if (resetPasswordToken.getConfirmedAt() != null) {
            throw new TokenAlreadyConfirmedException(TOKEN_ALREADY_USED.name(), TOKEN_CONFIRMED);
        }

        if (resetPasswordToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new TokenExpiredException(StatusCodes.TOKEN_EXPIRED.name(), TOKEN_EXPIRED);
        }
    }
}
