package com.epam.finaltask.service.impl;

import com.epam.finaltask.model.enums.VoucherStatus;
import com.epam.finaltask.repository.VoucherRepository;
import com.epam.finaltask.service.EmailSenderService;
import com.epam.finaltask.service.SchedulerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
    private final VoucherRepository voucherRepository;
    private final EmailSenderService emailSenderService;

    @Value("${scheduler.days.before.making.voucher.hot}")
    private int daysBeforeMakingVoucherHot;

    @Value("${scheduler.discount.percentage}")
    private int discountPercentage;

    @Value("${scheduler.days.to.arrive.reminder}")
    private int daysToArriveReminder;

    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void makeVoucherStatusNotSold() {
        log.info("Scheduled task - checking if vouchers are not sold");
        voucherRepository.findAll().forEach(voucher -> {
            if (voucher.getArrivalDate().isBefore(LocalDate.now())
                    && voucher.getStatus().equals(VoucherStatus.AVAILABLE)) {
                voucher.setStatus(VoucherStatus.NOT_SOLD);
                voucherRepository.save(voucher);
                log.info("Changed status to 'NOT_SOLD' for voucher: {}", voucher.getId());
            }
        });
    }

    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void makeVoucherHotAndReducePrice() {
        log.info("Scheduled task - making vouchers hot and reducing price");
        voucherRepository.findAll().forEach(voucher -> {
            if (voucher.getArrivalDate().isBefore(LocalDate.now().plusDays(daysBeforeMakingVoucherHot))
                    && voucher.getStatus().equals(VoucherStatus.AVAILABLE)
                    && !voucher.isHot()) {
                voucher.setHot(true);
                voucher.setPrice(voucher.getPrice() * (100 - discountPercentage) / 100);
                voucherRepository.save(voucher);
                log.info("Changed status to 'HOT' and reduced price for voucher: {}", voucher.getId());
            }
        });
    }

    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at 8 am
    @Scheduled(cron = "0 0 8 * * *")
    public void sendReminderAboutArrivalDate() {
        log.info("Scheduled task - sending reminders about arrival date");
        voucherRepository.findAll().forEach(voucher -> {
            if (voucher.getArrivalDate()
                    .compareTo(ChronoLocalDate.from(LocalDateTime.now().plusDays(daysToArriveReminder))) == 0
                    && (voucher.getStatus().equals(VoucherStatus.REGISTERED) || voucher.getStatus().equals(VoucherStatus.PAID))) {
                emailSenderService.sendReminderAboutArrivalDate(voucher);
                log.info("Arrival date reminder sent successfully to: {}", voucher.getUser().getEmail());
            }
        });
    }
}