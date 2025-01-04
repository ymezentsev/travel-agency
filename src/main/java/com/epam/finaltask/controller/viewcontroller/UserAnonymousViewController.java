package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.ChangePasswordRequest;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.ResetPasswordTokenService;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.finaltask.util.ViewControllerUtil.getErrors;

@Controller
@RequestMapping("/v1/users/anonymous")
@RequiredArgsConstructor
public class UserAnonymousViewController {
    private final UserService userService;
    private final EmailSenderService emailSenderService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final I18nUtil i18nUtil;

    @Value("${reset.password.token.lifetime}")
    private Long tokenLifetime;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "users/register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Validated(OnCreate.class) @ModelAttribute("userDto") UserDTO userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "users/register";
        }

        try {
            userService.register(userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/register";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-registered"));
        return "redirect:/v1/auth/login";
    }

    @GetMapping("/reset-password-request")
    public String getResetPasswordRequestPage() {
        return "users/reset-password-request";
    }

    @PostMapping("/reset-password-request")
    public String sendResetPasswordRequest(Model model,
                                           @RequestParam("username") String username,
                                           RedirectAttributes redirectAttributes) {

        try {
            emailSenderService.sendResetPasswordEmail(username);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/reset-password-request";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-reset-password-email", String.valueOf(tokenLifetime)));
        return "redirect:/v1/auth/login";
    }

    @GetMapping("/reset-password")
    public String getResetPasswordPage(Model model,
                                       @RequestParam("token") String token) {
        try {
            resetPasswordTokenService.validateResetPasswordToken(
                    resetPasswordTokenService.getResetPasswordToken(token));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/reset-password-request";
        }
        model.addAttribute("changePasswordRequestDto", new ChangePasswordRequest());
        model.addAttribute("token", token);
        return "users/reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(Model model,
                                @RequestParam("token") String token,
                                @Valid @ModelAttribute("changePasswordRequestDto")
                                ChangePasswordRequest request,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            model.addAttribute("token", token);
            return "users/reset-password";
        }

        try {
            userService.resetPassword(request.getNewPassword(), token);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/reset-password";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-password-change"));
        return "redirect:/v1/auth/login";
    }
}
