package com.epam.finaltask.controller;

import com.epam.finaltask.controller.openapi.AuthenticationControllerOpenApi;
import com.epam.finaltask.dto.AuthenticationRequest;
import com.epam.finaltask.dto.RefreshTokenRequest;
import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.service.AuthenticationService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.finaltask.model.enums.StatusCodes.OK;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthenticationController implements AuthenticationControllerOpenApi {
    private final AuthenticationService authenticationService;
    private final I18nUtil i18nUtil;

    @Override
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public RemoteResponse authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.auth-succeed"))
                .results(List.of(authenticationService.authenticate(authenticationRequest)))
                .build();
    }

    @Override
    @PostMapping("/refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.refresh-token-succeed"))
                .results(List.of(authenticationService.refreshToken(refreshTokenRequest)))
                .build();
    }
}
