package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.model.*;
import com.epam.finaltask.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.epam.finaltask.utils.ViewUtils.getErrors;
import static com.epam.finaltask.utils.ViewUtils.getPreviousPageUri;

@Controller
@RequestMapping("/v1/vouchers/auth-admin")
@RequiredArgsConstructor
@Validated
public class VoucherAuthAdminViewController {
    private final VoucherService voucherService;

    @GetMapping("/{voucherId}/delete")
    public String deleteVoucher(@PathVariable("voucherId") String voucherId,
                                Model model,
                                RedirectAttributes redirectAttributes,
                                HttpServletRequest request) {
        try {
            voucherService.delete(voucherId);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        redirectAttributes.addFlashAttribute("message", "Voucher deleted successfully");
        return "redirect:" + getPreviousPageUri(request);
    }

    @GetMapping("/create")
    public String getCreateVoucherPage(Model model,
                                       HttpServletRequest request) {
        model.addAttribute("voucherDto", new VoucherDTO());
        model.addAttribute("previousPage", getPreviousPageUri(request));
        return "create-voucher";
    }

    @PostMapping("/create")
    public String createVoucher(Model model,
                                @RequestParam("previousPage") String previousPage,
                                @Validated(OnCreate.class) @ModelAttribute("voucherDto") VoucherDTO voucherDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "create-voucher";
        }

        try {
            voucherService.create(voucherDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "create-voucher";
        }
        redirectAttributes.addFlashAttribute("message", "Voucher created successfully");
        return "redirect:" + previousPage;
    }

    @GetMapping("/{voucherId}/update")
    public String getUpdateVoucherPage(@PathVariable("voucherId") String voucherId,
                                     Model model,
                                     HttpServletRequest request) {
        model.addAttribute("voucherDto", voucherService.findById(voucherId));
        model.addAttribute("previousPage", getPreviousPageUri(request));
        return "update-voucher";
    }

    @PostMapping("/update")
    public String updateVoucher(Model model,
                                @RequestParam("previousPage") String previousPage,
                                @Validated(OnCreate.class) @ModelAttribute("voucherDto") VoucherDTO voucherDto,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "update-voucher";
        }

        try {
            voucherService.update(voucherDto.getId(), voucherDto);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "update-voucher";
        }
        redirectAttributes.addFlashAttribute("message", "Voucher updated successfully");
        return "redirect:" + previousPage;
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
        model.addAttribute("voucherStatuses", Arrays.stream(VoucherStatus.values())
                .filter(value -> !value.equals(VoucherStatus.REGISTERED) && !value.equals(VoucherStatus.PAID))
                .collect(Collectors.toList()));
    }
}
