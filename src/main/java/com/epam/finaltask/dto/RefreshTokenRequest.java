package com.epam.finaltask.dto;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequest(@NotBlank(message = "{validation.refresh-token-required}")
                                  String refreshToken) {
}
