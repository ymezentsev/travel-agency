package com.epam.finaltask.controller;

import com.epam.finaltask.auth.AuthenticationRequest;
import com.epam.finaltask.auth.AuthenticationResponse;
import com.epam.finaltask.auth.AuthenticationService;
import com.epam.finaltask.mapper.UserMapper;
import com.epam.finaltask.model.User;
import com.epam.finaltask.service.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/authentication")
@Slf4j
@Tag(name = "Authentication", description = "Users authentication")
public class AuthenticationControllerNoRest {
    private final AuthenticationService service;

    public AuthenticationControllerNoRest(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("/login")
    public String authenticate(Model model, @ModelAttribute("loginRequest") AuthenticationRequest loginRequest) {
        log.info("Start authentication for user={}", loginRequest.getUsername());

        AuthenticationResponse response = service.authenticate(loginRequest);

        if (response != null) { // Assuming service.authenticate returns null if authentication fails
            log.info("User={} was authenticated successfully", loginRequest.getUsername());
            model.addAttribute("message", "User is successfully authenticated");
            return "redirect:/vouchers/dashboard"; // Assuming you have a dashboard.html page to redirect to upon successful authentication
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "auth/sign-in"; // Redirect back to the login page if authentication fails
        }
    }

    @GetMapping("/sign-in")
    public String signIn(Model model){
        model.addAttribute("loginRequest", new AuthenticationRequest());
        return "auth/sign-in";
    }

}
