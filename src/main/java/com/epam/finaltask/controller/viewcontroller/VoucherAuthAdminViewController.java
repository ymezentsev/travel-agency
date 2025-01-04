package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.group.OnCreate;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.epam.finaltask.util.ViewControllerUtil.DEFAULT_PAGE_SIZE;
import static com.epam.finaltask.util.ViewControllerUtil.getErrors;
import static com.epam.finaltask.util.ViewControllerUtil.getPreviousPageUri;

@Controller
@RequestMapping("/v1/vouchers/auth-admin")
@RequiredArgsConstructor
@Validated
public class VoucherAuthAdminViewController {
    private final VoucherService voucherService;
    private final I18nUtil i18nUtil;

    @GetMapping("/{voucherId}/delete")
    public String deleteVoucher(@PathVariable("voucherId") String voucherId,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {
        try {
            voucherService.delete(voucherId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "vouchers/vouchers";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.voucher-deleted", voucherId));
        return "redirect:" + getPreviousPageUri(request);
    }

    @GetMapping("/create")
    public String getCreateVoucherPage(Model model,
                                       HttpServletRequest request) {
        model.addAttribute("voucherDto", new VoucherDTO());
        model.addAttribute("previousPage", getPreviousPageUri(request));
        return "vouchers/create-voucher";
    }

    @PostMapping("/create")
    public String createVoucher(Model model,
                                @RequestParam("previousPage") String previousPage,
                                @Validated(OnCreate.class) @ModelAttribute("voucherDto") VoucherDTO voucherDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            model.addAttribute("previousPage", previousPage);
            return "vouchers/create-voucher";
        }

        try {
            voucherService.create(voucherDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("previousPage", previousPage);
            return "vouchers/create-voucher";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.voucher-created"));
        return "redirect:" + previousPage;
    }

    @GetMapping("/{voucherId}/update")
    public String getUpdateVoucherPage(@PathVariable("voucherId") String voucherId,
                                       Model model,
                                       HttpServletRequest request) {
        model.addAttribute("voucherDto", voucherService.findById(voucherId));
        model.addAttribute("previousPage", getPreviousPageUri(request));
        getVoucherStatusesForUpdate(model);
        return "vouchers/update-voucher";
    }

    @PostMapping("/update")
    public String updateVoucher(Model model,
                                @RequestParam("previousPage") String previousPage,
                                @Validated(OnCreate.class) @ModelAttribute("voucherDto") VoucherDTO voucherDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            model.addAttribute("previousPage", previousPage);
            getVoucherStatusesForUpdate(model);
            return "vouchers/update-voucher";
        }

        try {
            voucherService.update(voucherDto.getId(), voucherDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            model.addAttribute("previousPage", previousPage);
            getVoucherStatusesForUpdate(model);
            return "vouchers/update-voucher";
        }
        redirectAttributes.addFlashAttribute("message",
                i18nUtil.getMessage("message.voucher-updated"));
        return "redirect:" + previousPage;
    }

    @GetMapping("/{userId}")
    public String getAllUsersVouchers(Model model,
                                      @PathVariable("userId") String userId,
                                      @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                              sort = {"status", "arrivalDate", "id"},
                                              direction = Sort.Direction.DESC) Pageable pageable) {
        model.addAttribute("myLinks", true);
        model.addAttribute("vouchers", voucherService.findAllByUserId(userId, pageable));
        return "vouchers/vouchers";
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        model.addAttribute("voucherStatuses", VoucherStatus.values());
    }

    private void getVoucherStatusesForUpdate(Model model) {
        model.addAttribute("voucherStatuses", Arrays.stream(VoucherStatus.values())
                .filter(value -> !value.equals(VoucherStatus.REGISTERED) && !value.equals(VoucherStatus.PAID))
                .collect(Collectors.toList()));
    }
}
