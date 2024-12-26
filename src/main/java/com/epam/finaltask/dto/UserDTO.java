package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.OnChangeBalance;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.validator.ValueOfEnum;
import com.epam.finaltask.model.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for user")
public class UserDTO {
    @NotBlank(groups = {OnChangeStatus.class, OnChangeBalance.class}, message = "Id is required")
    @Schema(description = "User id", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private String id;

    @NotBlank(groups = {OnCreate.class}, message = "Username is required")
    @Length(min = 2, max = 30, message = "Username's length must be from 2 to 30 characters long")
    @Pattern(regexp = "[A-z0-9]{2,30}", message = "Username must contain only characters and numbers")
    @Schema(description = "Username", example = "User1")
    private String username;

    @NotBlank(groups = {OnCreate.class}, message = "Password is required")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,30}$",
            message = "Your password must contain upper and lower case letters and numbers," +
                    " at least 7 and maximum 30 characters.Password cannot contains spaces")
    @Schema(description = "User password", example = "Qwerty123")
    private String password;

    @ValueOfEnum(enumClass = Role.class)
    @Schema(description = "User role", example = "USER", defaultValue = "USER")
    private String role;

    @Schema(description = "User vouchers")
    private List<VoucherDTO> vouchers;

    // @Length(min = 10, max = 14, message = "Phone number's length must be from 10 to 14 characters long")
    @Pattern(regexp = "\\d{10,14}", message = "Phone number must contain only numbers")
    @Schema(description = "User phone number", example = "0502464646")
    private String phoneNumber;

    @NotNull(groups = {OnChangeBalance.class}, message = "Balance is required")
    @DecimalMin(groups = {OnChangeBalance.class}, value = "0.01",
            message = "Balance must be positive number")
    @Schema(description = "User balance")
    private Double balance;

    @Schema(description = "User account status", defaultValue = "true")
    private boolean accountStatus;

    @Pattern(regexp = "^[A-z0-9._-]+@[A-z0-9.-]+\\.[A-z]{2,4}$", message = "Not valid email")
    @Schema(description = "User email", example = "johndoe@gmail.com")
    private String email;
}
