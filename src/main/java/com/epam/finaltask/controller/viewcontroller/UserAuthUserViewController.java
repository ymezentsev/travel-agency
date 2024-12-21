package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.ChangePasswordRequestDto;
import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnChangeBalance;
import com.epam.finaltask.model.User;
import com.epam.finaltask.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

import static com.epam.finaltask.utils.ViewUtils.getErrors;
import static com.epam.finaltask.utils.ViewUtils.getPreviousPageUri;

@Controller
@RequestMapping("/v1/users/auth-user")
@RequiredArgsConstructor
public class UserAuthUserViewController {
    private final UserService userService;

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
                             @Valid @ModelAttribute("userDto") UserDTO userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "users/update-user";
        }

        try {
            userService.updateUser(userDto.getUsername(), userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/update-user";
        }
        redirectAttributes.addFlashAttribute("message", "User updated successfully");
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
        redirectAttributes.addFlashAttribute("message", "Balance updated successfully");
        return "redirect:" + previousPage;
    }

    @GetMapping("/change-password")
    public String getChangePasswordPage(Model model) {
        model.addAttribute("changePasswordRequestDto", new ChangePasswordRequestDto());
        return "users/change-password";
    }

    @PostMapping("/change-password")
    public String changePassword(Model model,
                                 @Valid @ModelAttribute("changePasswordRequestDto")
                                 ChangePasswordRequestDto requestDto,
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
        redirectAttributes.addFlashAttribute("message", "Password changed successfully");
        return "redirect:/v1/users/auth-user/id/" + user.getId();
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user,
                              HttpServletRequest request) {
        if (user != null) {
            model.addAttribute("authUser", user);
        }

        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("queryString", request.getQueryString());
    }
}
