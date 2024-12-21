package com.epam.finaltask.dto;

import com.epam.finaltask.dto.validator.FieldMatch;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@FieldMatch(first = "newPassword", second = "repeatNewPassword", message = "New and repeat passwords do not match")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for change user password")
public class ChangePasswordRequestDto {

    @NotBlank(message = "Current password is required")
    @Schema(description = "User Current Password", example = "Qwerty123")
    private String oldPassword;

    @NotBlank(message = "New password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,30}$",
            message = "Your password must contain upper and lower case letters and numbers," +
                    " at least 7 and maximum 30 characters.Password cannot contains spaces")
    @Schema(description = "User new password", example = "Qwerty123")
    private String newPassword;


    @NotBlank(message = "Repeat new password is required")
    @Schema(description = "User new password", example = "Qwerty123")
    private String repeatNewPassword;
}
