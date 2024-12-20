package com.epam.finaltask.service;

import com.epam.finaltask.model.VoucherStatus;
import com.epam.finaltask.repository.VoucherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerServiceImpl implements SchedulerService {
    private final VoucherRepository voucherRepository;

    @Value("${scheduler.days.before.making.voucher.hot}")
    private int daysBeforeMakingVoucherHot;

    @Value("${scheduler.discount.percentage}")
    private int discountPercentage;

    @Value("${scheduler.days.to.arrive}")
    private int daysToArrive;


    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void makeVoucherStatusNotSold() {
        log.info("Checking if vouchers are not sold");
        voucherRepository.findAll().forEach(voucher -> {
            if (voucher.getArrivalDate().isBefore(LocalDate.now())
                    && voucher.getStatus().equals(VoucherStatus.AVAILABLE)) {
                voucher.setStatus(VoucherStatus.NOT_SOLD);
                voucherRepository.save(voucher);
                log.info("Changed voucher status to NOT_SOLD for voucher: {}", voucher.getId());
            }
        });
    }

    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    public void makeVoucherHotAndReducePrice() {
        log.info("Making vouchers hot and reducing price");
        voucherRepository.findAll().forEach(voucher -> {
            if (voucher.getArrivalDate().isBefore(LocalDate.now().plusDays(daysBeforeMakingVoucherHot))
                    && voucher.getStatus().equals(VoucherStatus.AVAILABLE)
                    && !voucher.isHot()) {
                voucher.setHot(true);
                voucher.setPrice(voucher.getPrice() * (100 - discountPercentage) / 100);
                voucherRepository.save(voucher);
                log.info("Changed voucher to HOT and reduced price for voucher: {}", voucher.getId());
            }
        });
    }

    //todo add send reminder
    @Override
    @Async("myAsyncPoolTaskExecutor")
    //Scheduled every day at 7 am
    @Scheduled(cron = "0 0 7 * * *")
    public void sendReminderAboutArrivalDate() {
        log.info("Sending reminders about arrival date");

       /* reservationRepository.findAll().forEach(reservation -> {
            if (reservation.getCheckInDate()
                    .compareTo(ChronoLocalDate.from(LocalDateTime.now().plusDays(DAYS_TO_CHECK_IN))) == 0) {
                reservation.getUsers().forEach(user -> {
                    log.info("Sending reservation reminder email to: {}", user.getEmail());
                    emailSenderService.send(
                            user.getEmail().toLowerCase(),
                            emailContentBuilderService.buildEmailContent(user.getFirstName(),
                                    null,
                                    reservation,
                                    EmailSubject.RESERVATION_REMINDER),
                            EmailSubject.RESERVATION_REMINDER.getSubject());
                    log.info("Reservation reminder email sent successfully to: {}", user.getEmail());
                });
            }
        });*/
    }
}