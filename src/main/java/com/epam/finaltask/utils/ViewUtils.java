package com.epam.finaltask.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;

public class ViewUtils {
    public static final int DEFAULT_PAGE_SIZE = 5;

    //todo add constants for templates and attributes
    public static List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    public static String getPreviousPageUri(HttpServletRequest request) {
        return request.getParameter("requestURI") + "?" + request.getParameter("queryString");
    }
}
