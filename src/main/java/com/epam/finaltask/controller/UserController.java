package com.epam.finaltask.controller;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "User", description = "Users func")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful registration", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error. For example, password len less then 8 characters", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Username or Email already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/register")
    public ResponseEntity<RemoteResponse> registerUser(
            @Parameter(description = "User data to register", required = true) @Validated(OnCreate.class) @RequestBody UserDTO userDto) {
        log.info("Start user registration with email={}", userDto.getUsername());

        UserDTO createdUserDto = userService.register(userDto);

        log.info("User with email={} registered successfully", userDto.getUsername());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "User is successfully registered", List.of(createdUserDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(successfulResponse);
    }

    @Operation(summary = "Update user data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful update", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error.", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Username or Email already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PatchMapping("/{username}")
    public ResponseEntity<RemoteResponse> updateUser(
            @Parameter(description = "User data to update", required = true) @PathVariable String username, @Validated @RequestBody UserDTO userDto) {
        log.info("Start updating user={} data", username);

        UserDTO updatedUserDto = userService.updateUser(username, userDto);

        log.info("User with email={} updated successfully", updatedUserDto.getUsername());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "User is successfully updated", List.of(updatedUserDto));
        return ResponseEntity.ok().body(successfulResponse);
    }

    @Operation(summary = "Obtain user by theirs username")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully obtained", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @GetMapping("/{username}")
    public ResponseEntity<RemoteResponse> getUserByUsername(@PathVariable("username") String username) {
        log.info("Start obtaining user with email={}", username);

        UserDTO userDTO = userService.getUserByUsername(username);

        log.info("User with email={} obtained successfully", userDTO.getUsername());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "User was obtained successfully", List.of(userDTO));
        return ResponseEntity.ok().body(successfulResponse);
    }

    @Operation(summary = "Change user account status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful update", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error.", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Username or Email already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/change/{username}")
    public ResponseEntity<RemoteResponse> changeUserAccountStatus(
            @Parameter(description = "User data to update", required = true) @PathVariable String username, @Validated @RequestBody UserDTO userDto) {
        log.info("Start changing account status for user={}", username);

        UserDTO updatedUserDto = userService.updateUser(username, userDto);

        log.info("User with email={} updated successfully", updatedUserDto.getUsername());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "User is successfully updated", List.of(updatedUserDto));
        return ResponseEntity.ok().body(successfulResponse);
    }
}
