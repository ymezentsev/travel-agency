package com.epam.finaltask.controller;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.EntityAlreadyExistsException;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.VoucherRepository;
import com.epam.finaltask.service.VoucherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Voucher", description = "Voucher func")
public class VoucherControllerNoRest {

    private final VoucherRepository voucherRepository;
    private final VoucherService voucherService;

    @GetMapping("profile/{userId}")
    public List<Voucher> getVouchersByUserId(@PathVariable UUID userId) {
        return voucherRepository.findAllByUserId(userId);
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("vouchers", voucherRepository.findAll());
        return "user/dashboard";
    }

    @GetMapping("/orderForm/{orderId}/{userId}")
    public String showOrderForm(@PathVariable String orderId, @PathVariable String userId, Model model) {
        try {
            Optional<Voucher> voucherDTO = voucherRepository.findById(UUID.fromString(orderId));
            model.addAttribute("voucher", voucherDTO);
            return "vouchers/order";
        } catch (Exception e) {
            return "redirect:/errorPage";
        }
    }


    @PostMapping("/order/{orderId}/{userId}")
    public String orderVoucher(@PathVariable String orderId, @PathVariable String userId, RedirectAttributes redirectAttributes) {
        log.info("Start voucher ordering with id={}", orderId);
        try {
            VoucherDTO orderedVoucherDto = voucherService.order(orderId, userId);
            log.info("Voucher with id={} ordered successfully", orderId);
            redirectAttributes.addFlashAttribute("successMessage", "Voucher is successfully ordered");
            redirectAttributes.addFlashAttribute("orderedVoucher", orderedVoucherDto);
        } catch (EntityAlreadyExistsException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Voucher has already been ordered");
        } catch (ValidationException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Validation Error. For example, title must contain only characters and numbers");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unexpected internal error");
        }
        return "redirect:/dashboard";
    }



}
