package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.model.*;
import com.epam.finaltask.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import static com.epam.finaltask.utils.ViewUtils.DEFAULT_PAGE_SIZE;

@Controller
@RequestMapping("/v1/vouchers/auth-user")
@RequiredArgsConstructor
public class VoucherAuthUserViewController {
    private final VoucherService voucherService;

    @PostMapping("/{voucherId}/{userId}")
    public String orderVoucher(Model model,
                               @PathVariable("voucherId") String voucherId,
                               @PathVariable("userId") String userId) {
        try {
            voucherService.order(voucherId, userId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "redirect:/v1/vouchers/auth-user";
    }

    @GetMapping()
    public String getAllUsersVouchers(Model model,
                                      @AuthenticationPrincipal User user,
                                      @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                              sort = {"arrivalDate", "id"},
                                              direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("myLinks", true);
        model.addAttribute("vouchers", voucherService.findAllByUserId(String.valueOf(user.getId()), pageable));
        return "vouchers";
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user) {
        if (user != null) {
            model.addAttribute("username", user.getUsername());
            model.addAttribute("userId", user.getId());
        }
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        model.addAttribute("voucherStatuses", VoucherStatus.values());
    }
}
