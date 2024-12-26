package com.epam.finaltask.service;

import com.epam.finaltask.model.ResetPasswordToken;
import com.epam.finaltask.model.User;

public interface ResetPasswordTokenService {
    ResetPasswordToken generateResetPasswordToken(User user);

    String saveResetPasswordToken(ResetPasswordToken token);

    ResetPasswordToken getResetPasswordToken(String token);

    void validateResetPasswordToken(ResetPasswordToken resetPasswordToken);
}
