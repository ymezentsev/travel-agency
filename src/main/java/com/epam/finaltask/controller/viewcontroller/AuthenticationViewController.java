package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.auth.AuthenticationRequest;
import com.epam.finaltask.auth.AuthenticationService;
import com.epam.finaltask.model.User;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static com.epam.finaltask.utils.ViewUtils.getErrors;

@Controller
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthenticationViewController {
    private final AuthenticationService authenticationService;

    @GetMapping("/login")
    public String authenticate(Model model) {
        model.addAttribute("loginRequest", new AuthenticationRequest());
        return "auth/login";
    }

    @PostMapping("/login")
    public String authenticate(Model model,
                               @Valid @ModelAttribute("loginRequest") AuthenticationRequest loginRequest,
                               BindingResult bindingResult,
                               HttpServletResponse response) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "auth/login";
        }

        try {
            authenticationService.authenticateWithCookie(loginRequest, response);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "auth/login";
        }
        return "redirect:/v1/auth/getDashboard";
    }

    @GetMapping("/getDashboard")
    public String getVoucherDashboard(@AuthenticationPrincipal User user) {
        if (user.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN")
                        || authority.getAuthority().equals("ROLE_MANAGER"))
        ) {
            return "redirect:/v1/vouchers/auth-manager";
        } else {
            return "redirect:/v1/vouchers/anonymous";
        }
    }
}
