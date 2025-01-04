package com.epam.finaltask.util;

public class ValidationRegExp {
    public static final String USERNAME_REGEXP = "[A-z0-9]{2,30}";
    public static final String PASSWORDS_REGEXP = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z]).{7,30}$";
    public static final String PHONE_NUMBER_REGEXP = "\\d{10,14}";
    public static final String EMAIL_REGEXP = "^[A-z0-9._-]+@[A-z0-9.-]+\\.[A-z]{2,4}$";
}
