package com.epam.finaltask.util;

import com.epam.finaltask.dto.UserSearchParamsDto;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.List;

public class WebControllerUtil {
    public static final int DEFAULT_PAGE_SIZE = 5;
    public static final String SPLITTER = "(\\*)";

    public static List<String> getErrors(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();
    }

    public static String getPreviousPageUri(HttpServletRequest request) {
        return request.getParameter("requestURI") + "?" + request.getParameter("queryString");
    }

    public static void updateVoucherSearchParam(VoucherSearchParamsDto searchParams) {
        if (searchParams.getTitles().length != 0) {
            searchParams.setTitles(searchParams.getTitles()[0].split(SPLITTER));
        }

        if (searchParams.getIsHot().isEmpty()) {
            searchParams.setIsHot(null);
        }
    }

    public static void updateUserSearchParam(UserSearchParamsDto searchParams) {
        if (searchParams.getUsernames().length != 0) {
            searchParams.setUsernames(searchParams.getUsernames()[0].split(SPLITTER));
        }

        if (searchParams.getPhoneNumbers().length != 0) {
            searchParams.setPhoneNumbers(searchParams.getPhoneNumbers()[0].split(SPLITTER));
        }

        if (searchParams.getEmails().length != 0) {
            searchParams.setEmails(searchParams.getEmails()[0].split(SPLITTER));
        }

        if (searchParams.getIsUnlocked().isEmpty()) {
            searchParams.setIsUnlocked(null);
        }
    }
}
