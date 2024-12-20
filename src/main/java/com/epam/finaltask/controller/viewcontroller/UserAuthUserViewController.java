package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnChangeBalance;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.epam.finaltask.utils.ViewUtils.getErrors;

@Controller
@RequestMapping("/v1/users/auth-user")
@RequiredArgsConstructor
public class UserAuthUserViewController {
    private final UserService userService;

    @GetMapping("/id/{userId}")
    public String getUserById(Model model,
                              @PathVariable("userId") String userId) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        return "user-info";
    }

    @GetMapping("/update/{userId}")
    public String getUpdateUserPage(Model model,
                                    @PathVariable("userId") String userId) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        return "update-user";
    }

    @PostMapping("/update")
    public String updateUser(Model model,
                             @Valid @ModelAttribute("userDto") UserDTO userDto,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "update-user";
        }

        try {
            userService.updateUser(userDto.getUsername(), userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "update-user";
        }
        redirectAttributes.addFlashAttribute("message", "User updated successfully");
        return "redirect:/v1/users/auth-user/id/" + userDto.getId();
    }

    @GetMapping("/balance/{userId}")
    public String getUpdateBalancePage(Model model,
                                       @PathVariable("userId") String userId) {
        model.addAttribute("userDto", userService.getUserById(UUID.fromString(userId)));
        return "update-balance";
    }

    @PostMapping("/balance")
    public String updateBalance(Model model,
                                @Validated(OnChangeBalance.class) @ModelAttribute("userDto") UserDTO userDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "update-balance";
        }

        try {
            userService.updateBalance(userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "update-balance";
        }
        redirectAttributes.addFlashAttribute("message", "Balance updated successfully");
        return "redirect:/v1/users/auth-user/id/" + userDto.getId();
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userId", user.getId());
        }
    }
}
