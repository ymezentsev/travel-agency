package com.epam.finaltask.controller.webcontroller;

import com.epam.finaltask.dto.UserDTO;
import com.epam.finaltask.dto.UserSearchParamsDto;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.Role;
import com.epam.finaltask.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.epam.finaltask.util.WebControllerUtil.*;

@Controller
@RequestMapping("/v1/users/auth-admin")
@RequiredArgsConstructor
public class UserAuthAdminWebController {
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

    @GetMapping("/by-role/{role}")
    public String getAllUsersByRole(Model model,
                                    @PathVariable("role") String role,
                                    @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                            sort = {"username"}) Pageable pageable) {
        UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                .roles(new String[]{role})
                .build();

        model.addAttribute("users", userService.search(searchParams, pageable));
        return "users/users";
    }

    @GetMapping("/by-lockedStatus/{isUnlocked}")
    public String getAllUsersByLockedStatus(Model model,
                                            @PathVariable("isUnlocked") String isUnlocked,
                                            @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                    sort = {"role", "username"}) Pageable pageable) {
        UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                .isUnlocked(isUnlocked)
                .build();

        model.addAttribute("users", userService.search(searchParams, pageable));
        return "users/users";
    }

    @GetMapping("/by-username")
    public String getAllUsersByUsername(Model model,
                                        @RequestParam("username") String username,
                                        @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                sort = {"role", "username"}) Pageable pageable) {
        try {
            UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                    .usernames(username.split(SPLITTER))
                    .build();

            model.addAttribute("users", userService.search(searchParams, pageable));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "users/users";
    }

    @GetMapping("/by-phoneNumber")
    public String getAllUsersByPhoneNumber(Model model,
                                           @RequestParam("phoneNumber") String phoneNumber,
                                           @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                   sort = {"role", "username"}) Pageable pageable) {
        try {
            UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                    .phoneNumbers(phoneNumber.split(SPLITTER))
                    .build();

            model.addAttribute("users", userService.search(searchParams, pageable));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "users/users";
    }

    @GetMapping("/by-email")
    public String getAllUsersByEmail(Model model,
                                     @RequestParam("email") String email,
                                     @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                             sort = {"role", "username"}) Pageable pageable) {
        try {
            UserSearchParamsDto searchParams = UserSearchParamsDto.builder()
                    .emails(email.split(SPLITTER))
                    .build();

            model.addAttribute("users", userService.search(searchParams, pageable));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "users/users";
    }

    @GetMapping("/search")
    public String getSearchPage(Model model) {
        model.addAttribute("searchParams", new UserSearchParamsDto());
        return "users/user-search";
    }

    @GetMapping("/search-result")
    public String search(Model model,
                         @ModelAttribute("searchParams") UserSearchParamsDto searchParams,
                         BindingResult bindingResult,
                         @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                 sort = {"role", "id"}) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "users/user-search";
        }

        updateUserSearchParam(searchParams);
        model.addAttribute("users", userService.search(searchParams, pageable));
        return "users/users";
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user,
                              HttpServletRequest request) {
        model.addAttribute("roles", Role.values());
        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("queryString", request.getQueryString());
    }
}
