package com.epam.finaltask.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class I18nUtil {
    private final MessageSource messageSource;

    public String getMessage(String message, String... args){
        return messageSource.getMessage(message, args, LocaleContextHolder.getLocale());
    }
}

