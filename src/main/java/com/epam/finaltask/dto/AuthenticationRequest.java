package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Dto for login user")
public class AuthenticationRequest {
    @NotBlank(message = "{validation.username-required}")
    @Schema(description = "Username", example = "User")
    private String username;

    @NotBlank(message = "{validation.password-required}")
    @Schema(description = "User password", example = "Qwerty123")
    private String password;
}
