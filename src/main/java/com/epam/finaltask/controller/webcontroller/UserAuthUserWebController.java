package com.epam.finaltask.controller.webcontroller;

import com.epam.finaltask.dto.ChangePasswordRequest;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnChangeBalance;
import com.epam.finaltask.dto.group.OnChangePassword;
import com.epam.finaltask.dto.group.OnUpdate;
import com.epam.finaltask.model.User;
import com.epam.finaltask.service.UserService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static com.epam.finaltask.util.WebControllerUtil.getErrors;
import static com.epam.finaltask.util.WebControllerUtil.getPreviousPageUri;

@Validated
@Controller
@RequestMapping("/v1/users/auth-user")
@RequiredArgsConstructor
public class UserAuthUserWebController {
    private final UserService userService;
    private final I18nUtil i18nUtil;

    @GetMapping("/id/{userId}")
    public String getUserById(Model model,
                              @PathVariable("userId") String userId) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        return "users/user-info";
    }

    @GetMapping("/update/{userId}")
    public String getUpdateUserPage(Model model,
                                    @PathVariable("userId") String userId) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        return "users/update-user";
    }

    @PostMapping("/update")
    public String updateUser(Model model,
                             @Validated(OnUpdate.class) @ModelAttribute("userDto") UserDTO userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "users/update-user";
        }

        try {
            userService.updateUser(UUID.fromString(userDto.getId()), userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/update-user";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-updated"));
        return "redirect:/v1/users/auth-user/id/" + userDto.getId();
    }

    @GetMapping("/balance/{userId}")
    public String getUpdateBalancePage(Model model,
                                       @PathVariable("userId") String userId,
                                       HttpServletRequest request) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        model.addAttribute("previousPage", getPreviousPageUri(request));
        return "users/update-balance";
    }

    @PostMapping("/balance")
    public String updateBalance(Model model,
                                @RequestParam("previousPage") String previousPage,
                                @Validated(OnChangeBalance.class) @ModelAttribute("userDto") UserDTO userDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            model.addAttribute("previousPage", previousPage);
            return "users/update-balance";
        }

        try {
            userService.updateBalance(userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("previousPage", previousPage);
            return "users/update-balance";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-balance-changed"));
        return "redirect:" + previousPage;
    }

    @GetMapping("/change-password")
    public String getChangePasswordPage(Model model) {
        model.addAttribute("changePasswordRequestDto", new ChangePasswordRequest());
        return "users/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model,
                                 @Validated(OnChangePassword.class) @ModelAttribute("changePasswordRequestDto")
                                 ChangePasswordRequest requestDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal User user,
                                 RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "users/change-password";
        }

        try {
            userService.changePassword(requestDto, user.getId());
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/change-password";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.user-password-changed"));
        return "redirect:/v1/users/auth-user/id/" + user.getId();
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user,
                              HttpServletRequest request) {
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("queryString", request.getQueryString());
    }
}
