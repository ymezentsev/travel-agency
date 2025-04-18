package com.epam.finaltask.service.impl;

import com.epam.finaltask.exception.FailedToSendEmailException;
import com.epam.finaltask.service.AsyncEmailService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import static com.epam.finaltask.model.enums.StatusCodes.INTERNAL_SERVER_ERROR;

@Service
@RequiredArgsConstructor
public class AsyncEmailServiceImpl implements AsyncEmailService {
    private final JavaMailSender mailSender;
    private final I18nUtil i18nUtil;

    @Value("${email.sender.login}")
    private String sender;

    @Override
    @Async("myAsyncPoolTaskExecutor")
    public void sendEmail(String to, String email, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "Utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom(sender);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new FailedToSendEmailException(INTERNAL_SERVER_ERROR.name(),
                    i18nUtil.getMessage("error.failed-to-send-email"));
        }
    }
}
