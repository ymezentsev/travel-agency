package com.epam.finaltask.controller.webcontroller;

import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.model.User;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.enums.VoucherStatus;
import com.epam.finaltask.service.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.epam.finaltask.util.WebControllerUtil.DEFAULT_PAGE_SIZE;
import static com.epam.finaltask.util.WebControllerUtil.getErrors;
import static com.epam.finaltask.util.WebControllerUtil.updateVoucherSearchParam;

@Controller
@RequestMapping("/v1/vouchers/anonymous")
@RequiredArgsConstructor
public class VoucherAnonymousWebController {
    private final VoucherService voucherService;

    @GetMapping("/index")
    public String getIndexPageWithHotAvailableVouchers(Model model,
                                                       @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                               sort = {"arrivalDate", "id"}) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .isHot("true")
                .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "index";
    }

    @GetMapping("/{voucherId}")
    public String getVoucherById(Model model,
                                 @PathVariable("voucherId") String voucherId) {
        try {
            model.addAttribute("voucher", voucherService.findById(voucherId));
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "vouchers/voucher-info";
    }

    @GetMapping()
    public String getAllAvailableVouchers(Model model,
                                          @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                  sort = {"isHot", "id"},
                                                  direction = Sort.Direction.DESC) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-tourType/{tourType}")
    public String getAllAvailableVouchersByTourType(@PathVariable("tourType") String tourType,
                                                    Model model,
                                                    @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                            sort = {"isHot", "id"},
                                                            direction = Sort.Direction.DESC) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                .tourTypes(new String[]{tourType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-transferType/{transferType}")
    public String getAllAvailableVouchersByTransferType(@PathVariable("transferType") String transferType,
                                                        Model model,
                                                        @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                                sort = {"isHot", "id"},
                                                                direction = Sort.Direction.DESC) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                .transferTypes(new String[]{transferType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-hotelType/{hotelType}")
    public String getAllAvailableVouchersByHotelType(@PathVariable("hotelType") String hotelType,
                                                     Model model,
                                                     @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                             sort = {"isHot", "id"},
                                                             direction = Sort.Direction.DESC) Pageable pageable) {
        VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                .hotelTypes(new String[]{hotelType})
                .build();

        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @GetMapping("/by-price")
    public String getAllAvailableVouchersByPrice(@RequestParam("maxPrice") String maxPrice,
                                                 Model model,
                                                 @PageableDefault(size = DEFAULT_PAGE_SIZE,
                                                         sort = {"isHot", "id"},
                                                         direction = Sort.Direction.DESC) Pageable pageable) {
        try {
            VoucherSearchParamsDto searchParams = VoucherSearchParamsDto.builder()
                    .voucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()})
                    .maxPrice(Double.parseDouble(maxPrice))
                    .build();
            model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        } catch (Exception e) {
            model.addAttribute("errors", e.getMessage());
        }
        return "vouchers/vouchers";
    }

    @GetMapping("/search")
    public String getSearchPage(Model model) {
        model.addAttribute("searchParams", new VoucherSearchParamsDto());
        return "vouchers/voucher-search";
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

        searchParams.setVoucherStatuses(new String[]{VoucherStatus.AVAILABLE.name()});
        updateVoucherSearchParam(searchParams);
        model.addAttribute("vouchers", voucherService.search(searchParams, pageable));
        return "vouchers/vouchers";
    }

    @ModelAttribute
    public void populateModel(Model model,
                              @AuthenticationPrincipal User user) {
        model.addAttribute("tourTypes", TourType.values());
        model.addAttribute("transferTypes", TransferType.values());
        model.addAttribute("hotelTypes", HotelType.values());
        model.addAttribute("voucherStatuses", VoucherStatus.values());

        model.addAttribute("myLinks", false);
    }
}
