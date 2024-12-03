package com.epam.finaltask.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "Dto for user")
public class UserDTO {
    private String id;

    @NotBlank(message = "Username is required")
    @Size(min = 2, max = 30, message = "Username's length must be from 2 to 30 characters long")
    @Pattern(regexp = "[A-z0-9]{2,30}", message = "Username must contain only characters and numbers")
    @Schema(description = "Username", example = "User1")
    private String username;

    @NotBlank(message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,30}$",
            message = "Your password must contain upper and lower case letters and numbers," +
                    " at least 7 and maximum 30 characters.Password cannot contains spaces")
    @Schema(description = "User password", example = "Qwerty123")
    private String password;

    private String role;
    private List<VoucherDTO> vouchers;

   // @Size(min = 10, max = 14, message = "Phone number's length must be from 10 to 14 characters long")
    @Pattern(regexp = "\\d{10,14}", message = "Phone number must contain only numbers")
    @Schema(description = "User phone number", example = "0502464646")
    private String phoneNumber;

    private Double balance;
    private boolean accountStatus;
}
