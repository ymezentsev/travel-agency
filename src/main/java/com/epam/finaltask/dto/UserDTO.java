package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.*;
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

import java.util.List;

import static com.epam.finaltask.util.ValidationRegExp.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for user")
public class UserDTO {
    @NotBlank(groups = {OnChangeStatus.class, OnChangeRole.class, OnChangeBalance.class},
            message = "{validation.id-required}")
    @Schema(description = "User id", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private String id;

    @NotBlank(groups = {OnCreate.class}, message = "{validation.username-required}")
    @Pattern(regexp = USERNAME_REGEXP, message = "{validation.username-validation-message}")
    @Schema(description = "Username", example = "User1")
    private String username;

    @NotBlank(groups = {OnCreate.class}, message = "{validation.password-required}")
    @Pattern(regexp = PASSWORDS_REGEXP, message = "{validation.password-validation-message}")
    @Schema(description = "User password", example = "Qwerty123")
    private String password;

    @NotBlank(groups = {OnChangeRole.class}, message = "{validation.role-required}")
    @ValueOfEnum(enumClass = Role.class, message = "{validation.enum-validation-message}")
    @Schema(description = "User role", example = "USER", defaultValue = "USER")
    private String role;

    @Schema(description = "User vouchers")
    private List<VoucherDTO> vouchers;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.phone-number-required}")
    @Pattern(regexp = PHONE_NUMBER_REGEXP, message = "{validation.phone-number-validation-message}")
    @Schema(description = "User phone number", example = "0502464646")
    private String phoneNumber;

    @NotNull(groups = {OnChangeBalance.class}, message = "{validation.balance-required}")
    @DecimalMin(groups = {OnChangeBalance.class}, value = "0.01", message = "{validation.balance-validation-message}")
    @Schema(description = "User balance")
    private Double balance;

    @Schema(description = "User account status", defaultValue = "true")
    private boolean accountStatus;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.email-required}")
    @Pattern(regexp = EMAIL_REGEXP, message = "{validation.email-validation-message}")
    @Schema(description = "User email", example = "johndoe@gmail.com")
    private String email;
}
