package com.epam.finaltask.controller.webcontroller;

import com.epam.finaltask.model.User;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
public class GlobalControllerAdvice {
    @ModelAttribute()
    public void populateModelWithUserAndLanguage(Model model,
                                                 @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("authUser", user);
        }
        model.addAttribute("locale", LocaleContextHolder.getLocale());
    }
}

