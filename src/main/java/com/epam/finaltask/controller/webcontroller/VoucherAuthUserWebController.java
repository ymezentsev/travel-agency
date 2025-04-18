package com.epam.finaltask.controller.webcontroller;

import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.enums.VoucherStatus;
import com.epam.finaltask.service.VoucherService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import static com.epam.finaltask.util.WebControllerUtil.DEFAULT_PAGE_SIZE;
import static com.epam.finaltask.util.WebControllerUtil.getPreviousPageUri;

@Controller
@RequestMapping("/v1/vouchers/auth-user")
@RequiredArgsConstructor
public class VoucherAuthUserWebController {
    private final VoucherService voucherService;
    private final I18nUtil i18nUtil;

    @PostMapping("/{voucherId}/{userId}")
    public String orderVoucher(Model model,
                               @PathVariable("voucherId") String voucherId,
                               @PathVariable("userId") String userId) {
        try {
            voucherService.order(voucherId, userId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "vouchers/vouchers";
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
        model.addAttribute("vouchers",
                voucherService.findAllByUserId(String.valueOf(user.getId()), pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/{voucherId}/cancelOrder")
    public String cancelVoucherOrder(Model model,
                                     @PathVariable("voucherId") String voucherId,
                                     HttpServletRequest request) {
        try {
            voucherService.cancelOrder(voucherId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("myLinks", true);
            return "vouchers/vouchers";
        }
        return "redirect:" + getPreviousPageUri(request);
    }


    @GetMapping("/{voucherId}/pay-confirmation")
    public String getPayConfirmationPage(Model model,
                                         @PathVariable("voucherId") String voucherId,
                                         HttpServletRequest request) {
        model.addAttribute("voucher", voucherService.findById(voucherId));
        model.addAttribute("previousPage", getPreviousPageUri(request));
        return "vouchers/pay-confirmation";
    }

    @GetMapping("/{voucherId}/pay")
    public String payVoucher(Model model,
                             @PathVariable("voucherId") String voucherId,
                             @RequestParam("previousPage") String previousPage,
                             RedirectAttributes redirectAttributes) {
        try {
            voucherService.payVoucher(voucherId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("voucher", voucherService.findById(voucherId));
            model.addAttribute("previousPage", previousPage);
            return "vouchers/pay-confirmation";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.voucher-payed"));
        return "redirect:" + previousPage;
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user,
                              HttpServletRequest request) {
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        model.addAttribute("voucherStatuses", VoucherStatus.values());

        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("queryString", request.getQueryString());
    }
}
