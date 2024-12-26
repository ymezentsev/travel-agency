package com.epam.finaltask.service.impl;

import com.epam.finaltask.exception.EntityNotFoundException;
import com.epam.finaltask.model.ResetPasswordToken;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.model.enums.EmailSubject;
import com.epam.finaltask.repository.UserRepository;
import com.epam.finaltask.service.AsyncEmailService;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.ResetPasswordTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static com.epam.finaltask.exception.StatusCodes.ENTITY_NOT_FOUND;
import static com.epam.finaltask.utils.ServiceUtils.USER_NOT_FOUND;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailSenderServiceImpl implements EmailSenderService {
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final AsyncEmailService asyncEmailService;
    private final UserRepository userRepository;
    private final TemplateEngine htmlTemplateEngine;

    @Value("${backend.base.url}")
    private String backendBaseUrl;

    @Value("${scheduler.days.to.arrive.reminder}")
    private int daysToArriveReminder;

    @Override
    public void sendResetPasswordEmail(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ENTITY_NOT_FOUND.name(), USER_NOT_FOUND));

        ResetPasswordToken resetPasswordToken = resetPasswordTokenService.generateResetPasswordToken(user);
        String token = resetPasswordTokenService.saveResetPasswordToken(resetPasswordToken);

        String emailContent = buildEmailContent(user.getUsername(), token,
                null, EmailSubject.RESET_PASSWORD);
        asyncEmailService.sendEmail(user.getEmail().toLowerCase(),
                emailContent, EmailSubject.RESET_PASSWORD.getSubject());
    }

    @Override
    public void sendOrderConfirmationEmail(Voucher voucher) {
        String emailContent = buildEmailContent(voucher.getUser().getUsername(), null,
                voucher, EmailSubject.VOUCHER_ORDER_CONFIRMATION);
        asyncEmailService.sendEmail(voucher.getUser().getEmail().toLowerCase(),
                emailContent, EmailSubject.VOUCHER_ORDER_CONFIRMATION.getSubject());
    }

    @Override
    public void sendPaymentConfirmationEmail(Voucher voucher) {
        String emailContent = buildEmailContent(voucher.getUser().getUsername(), null,
                voucher, EmailSubject.VOUCHER_PAYMENT_CONFIRMATION);
        asyncEmailService.sendEmail(voucher.getUser().getEmail().toLowerCase(),
                emailContent, EmailSubject.VOUCHER_PAYMENT_CONFIRMATION.getSubject());
    }

    @Override
    public void sendOrderCanceledEmail(Voucher voucher, User user) {
        String emailContent = buildEmailContent(user.getUsername(), null,
                voucher, EmailSubject.VOUCHER_ORDER_CANCELED);
        asyncEmailService.sendEmail(user.getEmail().toLowerCase(),
                emailContent,
                EmailSubject.VOUCHER_ORDER_CANCELED.getSubject());
    }

    @Override
    public void sendReminderAboutArrivalDate(Voucher voucher) {
        String emailContent = buildEmailContent(voucher.getUser().getUsername(), null,
                voucher, EmailSubject.VOUCHER_ARRIVAL_DATE_REMINDER);
        asyncEmailService.sendEmail(voucher.getUser().getEmail().toLowerCase(),
                emailContent,
                EmailSubject.VOUCHER_ARRIVAL_DATE_REMINDER.getSubject());
    }

    private String buildEmailContent(String username, String token, Voucher voucher, EmailSubject subject) {
        String template = subject.getTemplate();

        Context ctx = new Context();
        ctx.setVariable("username", username);
        if (token != null) {
            ctx.setVariable("url", backendBaseUrl + subject.getLink(token));
        }

        if (voucher != null) {
            ctx.setVariable("daysToArrive", daysToArriveReminder);
            ctx.setVariable("voucher", voucher);
        }
        return htmlTemplateEngine.process(template, ctx);
    }
}
