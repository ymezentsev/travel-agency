package com.epam.finaltask.controller;

import com.epam.finaltask.auth.AuthenticationRequest;
import com.epam.finaltask.auth.AuthenticationResponse;
import com.epam.finaltask.auth.AuthenticationService;
import com.epam.finaltask.dto.RemoteResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Authentication", description = "Users authentication")
public class AuthenticationController {

    private final AuthenticationService service;

    @Operation(summary = "User authentication")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Successful authentication",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RemoteResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized. For example, user's credentials are missing or invalid",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RemoteResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Forbidden. For example, " +
                            "the user is authenticated but does not have sufficient privileges " +
                            "to access the requested resource",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RemoteResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. For example, " +
                            "the client's request is malformed or missing required parameters, " +
                            "making it impossible to authenticate the user properly.",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RemoteResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "423",
                    description = "Locked. For example, " +
                            "the user account is temporarily locked, such as " +
                            "after multiple failed authentication attempts.",
                    content = @Content(
                            schema = @Schema(
                                    implementation = RemoteResponse.class
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            schema = @Schema(
                                    implementation = Error.class
                            )
                    )
            )
    })
    @PostMapping("/login")
    public ResponseEntity<RemoteResponse> authenticate(
            @RequestBody @Valid AuthenticationRequest request
    ) {
        log.info(
                "Start authentication for user={}",
                request.getUsername()
        );

        ResponseEntity<AuthenticationResponse> response =
                ResponseEntity.ok(
                        service.authenticate(request)
                );

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info(
                    "User={} was authenticated successfully",
                    request.getUsername()
            );
        }


        RemoteResponse successfulResponse = RemoteResponse
                .create(true,
                        OK.name(),
                        "User is successfully authenticated",
                        List.of(response.getBody())
                );
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(successfulResponse);
    }



}
