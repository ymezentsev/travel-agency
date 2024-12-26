package com.epam.finaltask.service;

import com.epam.finaltask.model.User;
import com.epam.finaltask.model.Voucher;

public interface EmailSenderService {
    void sendResetPasswordEmail(String username);

    void sendOrderConfirmationEmail(Voucher voucher);

    void sendPaymentConfirmationEmail(Voucher voucher);

    void sendOrderCanceledEmail(Voucher voucher, User user);

    void sendReminderAboutArrivalDate(Voucher voucher);
}

