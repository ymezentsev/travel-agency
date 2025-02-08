package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.OnChangePassword;
import com.epam.finaltask.dto.validator.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static com.epam.finaltask.util.ValidationRegExp.PASSWORDS_REGEXP;

@Data
@FieldMatch(first = "newPassword", second = "repeatNewPassword", message = "{validation.passwords-not-match}")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for change and reset user password")
public class ChangePasswordRequest {

    @NotBlank(groups = {OnChangePassword.class}, message = "{validation.current-password-required}")
    @Schema(description = "User Current Password", example = "Qwerty123")
    private String oldPassword;

    @NotBlank(message = "{validation.new-password-required}")
    @Pattern(regexp = PASSWORDS_REGEXP, message = "{validation.password-validation-message}")
    @Schema(description = "User new password", example = "Qwerty123")
    private String newPassword;

    @NotBlank(message = "{validation.repeat-password-required}")
    @Schema(description = "User new password", example = "Qwerty123")
    private String repeatNewPassword;
}
