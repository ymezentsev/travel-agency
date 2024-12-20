package com.epam.finaltask.auth;

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
    @NotBlank(message = "Username is required")
    @Schema(description = "Username", example = "User1")
    String username;

    @NotBlank(message = "Password is required")
    @Schema(description = "User password", example = "Qwerty123")
    String password;
}
