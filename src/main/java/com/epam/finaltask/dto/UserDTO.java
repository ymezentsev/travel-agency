package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.model.Voucher;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "User Data Transfer Object, specify role:USER to use this type of DTO")
public class UserDTO {
    @Schema(description = "User id")
    private String id;

    @NotBlank(message = "Name is required field")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Username must contain only characters and numbers")
    @Schema(description = "User name", required = true)
    private String username;

    @NotBlank(message = "Password is required field")
    @Pattern(regexp = "(^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{7,30}$)?",
            message = "Your password must contain upper and lower case letters and numbers, at least 7 and maximum 30 characters." +
                    "Password cannot contains spaces")
    @Schema(description = "User password", required = true)
    private String password;

    @Schema(description = "User role", defaultValue = "USER")
    private String role;

    @Schema(description = "User vouchers")
    @Valid
    private List<Voucher> vouchers;

    @Pattern(regexp = "^[0-9]+$", message = "Phone number must contain only numbers")
    @Schema(description = "User phone number")
    private String phoneNumber;

    @Schema(description = "User balance")
    private double balance;

    @Schema(description = "User account status", defaultValue = "true")
    private boolean accountStatus;
}
