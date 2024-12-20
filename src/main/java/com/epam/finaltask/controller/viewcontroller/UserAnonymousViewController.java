package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.finaltask.utils.ViewUtils.getErrors;

@Controller
@RequestMapping("/v1/users")
@RequiredArgsConstructor
public class UserAnonymousViewController {
    private final UserService userService;

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("userDto", new UserDTO());
        return "register";
    }

    @PostMapping("/register")
    public String register(Model model,
                           @Validated(OnCreate.class) @ModelAttribute("userDto") UserDTO userDto,
                           BindingResult bindingResult,
                           RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "register";
        }

        try {
            userService.register(userDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "register";
        }
        redirectAttributes.addFlashAttribute("message", "User registered successfully");
        return "redirect:/v1/auth/login";
    }
}
