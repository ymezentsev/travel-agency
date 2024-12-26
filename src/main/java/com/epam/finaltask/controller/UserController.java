package com.epam.finaltask.controller;

import com.epam.finaltask.controller.openapi.UserControllerOpenApi;
import com.epam.finaltask.dto.*;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.ResetPasswordTokenService;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static com.epam.finaltask.exception.StatusCodes.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController implements UserControllerOpenApi {
    private final UserService userService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailSenderService emailSenderService;

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_CREATED = "User is successfully registered";
    private static final String STATUS_MESSAGE_UPDATED = "User is successfully updated";
    private static final String STATUS_MESSAGE_GET = "User was obtained successfully";
    private static final String STATUS_MESSAGE_CHANGED = "User's account status is successfully changed";

    @Override
    @PostMapping("/register")
    public ResponseEntity<RemoteResponse> registerUser(@Validated(OnCreate.class)
                                                       @RequestBody UserDTO userDto) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CREATED, List.of(userService.register(userDto))),
                HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/{username}")
    public ResponseEntity<RemoteResponse> updateUser(@PathVariable("username") String username,
                                                     @Valid @RequestBody UserDTO userDto) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_UPDATED, List.of(userService.updateUser(username, userDto))),
                HttpStatus.OK);
    }

    @Override
    @PatchMapping("/accountStatus")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> changeAccountStatus(@Validated(OnChangeStatus.class)
                                                              @RequestBody UserDTO userDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CHANGED, List.of(userService.changeAccountStatus(userDTO))),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/{username}")
    public ResponseEntity<RemoteResponse> getUserByUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_GET, List.of(userService.getUserByUsername(username))),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/id/{userId}")
    public ResponseEntity<RemoteResponse> getUserById(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_GET, List.of(userService.getUserById(UUID.fromString(userId)))),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> search(UserSearchParamsDto params, Pageable pageable) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, List.of(userService.search(params, pageable))),
                HttpStatus.OK);
    }

    //todo add swagger
    @PostMapping("/reset-password")
    public void resetPassword(@RequestParam("username") String username) {
        emailSenderService.sendResetPasswordEmail(username);
    }

    //todo add swagger
    @GetMapping("/reset-password")
    //todo add page for set new password
    public ResponseEntity<Void> getResetPassword(@RequestParam("token") String token) {
        HttpHeaders headers = new HttpHeaders();

        resetPasswordTokenService.validateResetPasswordToken(
                resetPasswordTokenService.getResetPasswordToken(token));
        // headers.add("Location", frontendBaseUrl + TOKEN_RESET_PASSWORD_URL + token);
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }

    //todo add swagger
    @PutMapping("/reset-password")
    public void resetPassword(@RequestParam("token") String token,
                              @RequestBody @Valid ChangePasswordRequestDto request) {
        userService.resetPassword(request.getNewPassword(), token);
    }
}
