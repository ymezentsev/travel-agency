package com.epam.finaltask.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EmailSubject {
    //todo update getTemplate and getLink
    RESET_PASSWORD("Reset password instruction") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_RESET_PASSWORD_EMAIL;
        }

        @Override
        public String getLink(String token) {
            return TOKEN_RESET_PASSWORD_URL + token;
        }
    },

    VOUCHER_ORDER_CONFIRMATION("Voucher order confirmation") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_VOUCHER_ORDER_CONFIRMATION;
        }
    },

    VOUCHER_PAYMENT_CONFIRMATION("Voucher payment confirmation") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_VOUCHER_PAYMENT_CONFIRMATION;
        }
    },

    VOUCHER_ARRIVAL_DATE_REMINDER("Arrival date reminder") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_ARRIVAL_DATE_REMINDER;
        }
    },

    VOUCHER_ORDER_CANCELED("Reservation was canceled") {
        @Override
        public String getTemplate() {
            return TEMPLATE_FOR_VOUCHER_ORDER_CANCELED;
        }
    };

    private final String subject;

    private static final String TOKEN_RESET_PASSWORD_URL = "/v1/users/anonymous/reset-password?token=";
    private static final String TEMPLATE_FOR_RESET_PASSWORD_EMAIL = "emails/reset-password-email";
    private static final String TEMPLATE_FOR_ARRIVAL_DATE_REMINDER = "emails/arrival-date-reminder";
    private static final String TEMPLATE_FOR_VOUCHER_ORDER_CONFIRMATION = "emails/voucher-order-confirmation";
    private static final String TEMPLATE_FOR_VOUCHER_PAYMENT_CONFIRMATION = "emails/voucher-payment-confirmation";
    private static final String TEMPLATE_FOR_VOUCHER_ORDER_CANCELED = "emails/voucher-order-canceled";

    public abstract String getTemplate();

    public String getLink(String token) {
        return null;
    }
}