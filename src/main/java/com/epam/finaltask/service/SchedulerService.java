package com.epam.finaltask.service;

public interface SchedulerService {
    void makeVoucherStatusNotSold();

    void makeVoucherHotAndReducePrice();

    void sendReminderAboutArrivalDate();
}
