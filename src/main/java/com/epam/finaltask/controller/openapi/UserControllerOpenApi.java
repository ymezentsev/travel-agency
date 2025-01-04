package com.epam.finaltask.controller.openapi;

import com.epam.finaltask.dto.*;
import com.epam.finaltask.dto.group.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@Tag(name = "User Controller", description = "API to work with users")
public interface UserControllerOpenApi {

    @Operation(summary = "Register a new user")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "User successfully registered",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid user data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Username already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse registerUser(@Validated(OnCreate.class) @RequestBody UserDTO userDto);

    @Operation(summary = "Update user (current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid user data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse updateUser(@PathVariable("userId") UUID userId,
                              @Validated(OnUpdate.class) @RequestBody UserDTO userDto);

    @Operation(summary = "Get user by username (admin or manager only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse getUserByUsername(@PathVariable("username") String username);

    @Operation(summary = "Get user by id (admin, manager or current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse getUserById(@PathVariable("userId") UUID userId);

    @Operation(summary = "Get page of all users (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse getAllUsers(Pageable pageable);

    @Operation(summary = "Get page of users filtered by username, role, phone number, email, " +
            "isUnlocked status, and sorted by chosen parameters (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Users successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse search(UserSearchParamsDto params, Pageable pageable);

    @Operation(summary = "Change user's account status (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's account status successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing required parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse changeAccountStatus(@Validated(OnChangeStatus.class) @RequestBody UserDTO userDTO);

    @Operation(summary = "Change user's role (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's role successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing required parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse changeRole(@Validated(OnChangeRole.class) @RequestBody UserDTO userDTO);

    @Operation(summary = "Change user's balance (current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's balance successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing required parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse changeBalance(@Validated(OnChangeBalance.class) @RequestBody UserDTO userDTO);

    @Operation(summary = "Change user's password (current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's password successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing required parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse changePassword(@Validated(OnChangePassword.class)
                                  @RequestBody ChangePasswordRequest requestDto,
                                  @PathVariable("userId") UUID userId);

    @Operation(summary = "Send email to reset user's password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Reset password email sent successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "User not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse sendResetPasswordEmail(@RequestParam("username") String username);

    @Operation(summary = "Reset user's password")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User's password successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Missing required parameters",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "401",
                    description = "Token has expired",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Token not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Token has already been used",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    RemoteResponse resetPassword(@RequestParam("token") String token,
                                 @RequestBody @Valid ChangePasswordRequest request);
}
