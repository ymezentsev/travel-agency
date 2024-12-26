package com.epam.finaltask.service;

public interface AsyncEmailService {
    void sendEmail(String to, String email, String subject);
}

