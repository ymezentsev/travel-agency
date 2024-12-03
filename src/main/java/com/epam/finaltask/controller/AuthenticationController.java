package com.epam.finaltask.controller;

import com.epam.finaltask.auth.AuthenticationRequest;
import com.epam.finaltask.auth.AuthenticationService;
import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.exception.StatusCodes;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    private static final String STATUS_AUTH_MESSAGE = "User is successfully authenticated";

    @PostMapping("/login")
    public ResponseEntity<RemoteResponse> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_AUTH_MESSAGE, List.of(authenticationService.authenticate(authenticationRequest))),
                HttpStatus.ACCEPTED);
    }
}
