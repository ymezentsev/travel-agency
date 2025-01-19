package com.epam.finaltask.config;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.i18n.AbstractLocaleResolver;

import java.util.Arrays;
import java.util.Locale;

public class CustomLocaleResolver extends AbstractLocaleResolver {
    @Override
    public Locale resolveLocale(HttpServletRequest request) {
        Locale languageAttribute = (Locale) request.getAttribute("language");
        if (languageAttribute != null) {
            return languageAttribute;
        }

        if (request.getCookies() != null) {
            Locale langFromCookie = Arrays.stream(request.getCookies())
                    .filter(cookie -> "lang".equals(cookie.getName()))
                    .map(cookie -> Locale.forLanguageTag(cookie.getValue()))
                    .findFirst()
                    .orElse(null);
            if (langFromCookie != null) {
                return langFromCookie;
            }
        }

        String acceptLanguageHeader = request.getHeader("Accept-Language");
        if (StringUtils.hasText(acceptLanguageHeader)) {
            return Locale.forLanguageTag(acceptLanguageHeader);
        }
        return Locale.getDefault();
    }

    @Override
    public void setLocale(HttpServletRequest request, HttpServletResponse response, Locale locale) {
        String lang = request.getParameter("lang");
        if (lang != null) {
            locale = Locale.forLanguageTag(lang);
            request.setAttribute("language", Locale.forLanguageTag(lang));

            Cookie cookie = new Cookie("lang", locale.toLanguageTag());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
        }
    }
}
