package com.epam.finaltask.controller;

import com.epam.finaltask.controller.openapi.UserControllerOpenApi;
import com.epam.finaltask.dto.ChangePasswordRequest;
import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.UserSearchParamsDto;
import com.epam.finaltask.dto.group.*;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.epam.finaltask.model.enums.StatusCodes.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final I18nUtil i18nUtil;

    @Value("${reset.password.token.lifetime}")
    private Long tokenLifetime;

    @Override
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public RemoteResponse registerUser(@Validated(OnCreate.class) @RequestBody UserDTO userDto) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-registered"))
                .results(List.of(userService.register(userDto)))
                .build();
    }

    @Override
    @PutMapping("/{userId}")
    @PreAuthorize("@authenticationServiceImpl.isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse updateUser(@PathVariable("userId") UUID userId,
                                     @Validated(OnUpdate.class) @RequestBody UserDTO userDto) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-updated"))
                .results(List.of(userService.updateUser(userId, userDto)))
                .build();
    }

    @Override
    @GetMapping("/{username}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse getUserByUsername(@PathVariable("username") String username) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(userService.getUserByUsername(username)))
                .build();
    }

    @Override
    @GetMapping("/id/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @authenticationServiceImpl.isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse getUserById(@PathVariable("userId") UUID userId) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(userService.getUserById(userId)))
                .build();
    }

    @Override
    @GetMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse getAllUsers(Pageable pageable) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(userService.getAllUsers(pageable)))
                .build();
    }

    @Override
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse search(UserSearchParamsDto params, Pageable pageable) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(userService.search(params, pageable)))
                .build();
    }

    @Override
    @PatchMapping("/accountStatus")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changeAccountStatus(@Validated(OnChangeStatus.class) @RequestBody UserDTO userDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-status-changed"))
                .results(List.of(userService.changeAccountStatus(userDTO)))
                .build();
    }

    @Override
    @PatchMapping("/role")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changeRole(@Validated(OnChangeRole.class) @RequestBody UserDTO userDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-role-changed"))
                .results(List.of(userService.changeRole(userDTO)))
                .build();
    }

    @Override
    @PatchMapping("/balance")
    @PreAuthorize("@authenticationServiceImpl.isCurrentUser(#userDTO.getId())")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changeBalance(@Validated(OnChangeBalance.class) @RequestBody UserDTO userDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-balance-changed"))
                .results(List.of(userService.updateBalance(userDTO)))
                .build();
    }

    @Override
    @PatchMapping("/change-password/{userId}")
    @PreAuthorize("@authenticationServiceImpl.isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changePassword(@Validated(OnChangePassword.class)
                                         @RequestBody ChangePasswordRequest requestDto,
                                         @PathVariable("userId") UUID userId) {
        userService.changePassword(requestDto, userId);
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-password-changed"))
                .build();
    }

    @Override
    @PostMapping("/reset-password-email")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse sendResetPasswordEmail(@RequestParam("username") String username) {
        emailSenderService.sendResetPasswordEmail(username);
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-reset-password-email",
                        String.valueOf(tokenLifetime)))
                .build();
    }

    @Override
    @PatchMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse resetPassword(@RequestParam("token") String token,
                                        @RequestBody @Valid ChangePasswordRequest request) {
        userService.resetPassword(request.getNewPassword(), token);
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.user-password-changed"))
                .build();
    }
}
