package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.model.User;
import com.epam.finaltask.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.epam.finaltask.utils.ViewUtils.DEFAULT_PAGE_SIZE;
import static com.epam.finaltask.utils.ViewUtils.getPreviousPageUri;

@Controller
@RequestMapping("/v1/users/auth-admin")
@RequiredArgsConstructor
public class UserAuthAdminViewController {
    private final UserService userService;

    @GetMapping()
    public String getAllUsers(Model model,
                              @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                      sort = {"role", "username"}) Pageable pageable) {
        model.addAttribute("users", userService.getAllUsers(pageable));
        return "users/users";
    }

    @GetMapping("/account-status/{userId}")
    public String changeAccountStatus(Model model,
                                      @PathVariable("userId") String userId,
                                      @RequestParam("accountStatus") boolean accountStatus,
                                      HttpServletRequest request) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setAccountStatus(accountStatus);

            userService.changeAccountStatus(userDTO);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/users";
        }
        return "redirect:" + getPreviousPageUri(request);
    }

    @GetMapping("/role/{userId}")
    public String changeRole(Model model,
                             @PathVariable("userId") String userId,
                             @RequestParam("role") String role,
                             HttpServletRequest request) {
        try {
            UserDTO userDTO = new UserDTO();
            userDTO.setId(userId);
            userDTO.setRole(role);

            userService.changeRole(userDTO);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "users/users";
        }
        return "redirect:" + getPreviousPageUri(request);
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
