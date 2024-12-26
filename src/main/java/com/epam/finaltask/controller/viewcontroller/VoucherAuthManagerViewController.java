package com.epam.finaltask.controller.viewcontroller;

import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.model.*;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.enums.VoucherStatus;
import com.epam.finaltask.service.VoucherService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.epam.finaltask.utils.ViewUtils.*;

@Controller
@RequestMapping("/v1/vouchers/auth-manager")
@RequiredArgsConstructor
public class VoucherAuthManagerViewController {
    private final VoucherService voucherService;

    @GetMapping()
    public String getAllVouchers(Model model,
                                 @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                         sort = {"status", "arrivalDate", "id"}) Pageable pageable) {
        model.addAttribute("vouchers", voucherService.findAll(pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-voucherStatus/{voucherStatus}")
    public String getAllVouchersByVoucherStatus(@PathVariable("voucherStatus") String voucherStatus,
                                                Model model,
                                                @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                        sort = {"arrivalDate", "id"}) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .voucherStatuses(new String[]{voucherStatus})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-tourType/{tourType}")
    public String getAlVouchersByTourType(@PathVariable("tourType") String tourType,
                                          Model model,
                                          @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                  sort = {"status", "arrivalDate", "id"}) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .tourTypes(new String[]{tourType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-transferType/{transferType}")
    public String getAllVouchersByTransferType(@PathVariable("transferType") String transferType,
                                               Model model,
                                               @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                       sort = {"status", "arrivalDate", "id"}) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .transferTypes(new String[]{transferType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-hotelType/{hotelType}")
    public String getAllVouchersByHotelType(@PathVariable("hotelType") String hotelType,
                                            Model model,
                                            @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                    sort = {"status", "arrivalDate", "id"}) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .hotelTypes(new String[]{hotelType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-price")
    public String getAllVouchersByPrice(@RequestParam("maxPrice") String maxPrice,
                                        Model model,
                                        @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                sort = {"status", "arrivalDate", "id"}) Pageable pageable) {
        try {
            VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                    .maxPrice(Double.parseDouble(maxPrice))
                    .build();
            model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "vouchers/vouchers";
    }

    @GetMapping("/{voucherId}")
    public String changeVoucherHotStatus(@PathVariable("voucherId") String voucherId,
                                         @RequestParam("newHotStatus") String newHotStatus,
                                         Model model,
                                         HttpServletRequest request) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setIsHot(newHotStatus);

        try {
            voucherService.changeHotStatus(voucherId, voucherDTO);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "vouchers/vouchers";
        }
        return "redirect:" + getPreviousPageUri(request);
    }

    @GetMapping("/{voucherId}/status")
    public String changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                      @RequestParam("newStatus") String newStatus,
                                      Model model,
                                      HttpServletRequest request) {
        VoucherDTO voucherDTO = new VoucherDTO();
        voucherDTO.setStatus(newStatus);

        try {
            voucherService.changeStatus(voucherId, voucherDTO);
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
            return "vouchers/vouchers";
        }
        return "redirect:" + getPreviousPageUri(request);
    }

    @GetMapping("/search-result")
    public String search(Model model,
                         @ModelAttribute("searchParams") VoucherSearchParamsDto searchParams,
                         BindingResult bindingResult,
                         @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                 sort = {"isHot", "id"},
                                 direction = Sort.Direction.DESC) Pageable pageable) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", getErrors(bindingResult));
            return "vouchers/voucher-search";
        }

        updateVoucherSearchParam(searchParams);
        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }


    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user,
                              HttpServletRequest request) {
        if (user != null) {
            model.addAttribute("authUser", user);
        }
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        model.addAttribute("voucherStatuses", VoucherStatus.values());

        model.addAttribute("requestURI", request.getRequestURI());
        model.addAttribute("queryString", request.getQueryString());

        model.addAttribute("myLinks", false);
    }
}
