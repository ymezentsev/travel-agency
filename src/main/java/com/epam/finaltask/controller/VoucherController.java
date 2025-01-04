package com.epam.finaltask.controller;

import com.epam.finaltask.controller.openapi.VoucherControllerOpenApi;
import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import com.epam.finaltask.service.VoucherService;
import com.epam.finaltask.util.I18nUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.epam.finaltask.model.enums.StatusCodes.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/vouchers")
public class VoucherController implements VoucherControllerOpenApi {
    private final VoucherService voucherService;
    private final I18nUtil i18nUtil;

    @Override
    @PostMapping()
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public RemoteResponse createVoucher(@Validated(OnCreate.class) @RequestBody VoucherDTO voucherDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-created"))
                .results(List.of(voucherService.create(voucherDTO)))
                .build();
    }

    @Override
    @PutMapping("/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse updateVoucher(@PathVariable("voucherId") String voucherId,
                                        @Validated(OnUpdate.class) @RequestBody VoucherDTO voucherDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-updated"))
                .results(List.of(voucherService.update(voucherId, voucherDTO)))
                .build();
    }

    @Override
    @DeleteMapping("/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse deleteVoucherById(@PathVariable("voucherId") String voucherId) {
        voucherService.delete(voucherId);
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-deleted", voucherId))
                .build();
    }

    @Override
    @PatchMapping("/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changeVoucherHotStatus(@PathVariable("voucherId") String voucherId,
                                                 @Valid @RequestBody VoucherDTO voucherDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-hot-status-changed"))
                .results(List.of(voucherService.changeHotStatus(voucherId, voucherDTO)))
                .build();
    }

    @Override
    @PatchMapping("/status/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                              @Validated(OnChangeStatus.class)
                                              @RequestBody VoucherDTO voucherDTO) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-status-changed"))
                .results(List.of(voucherService.changeStatus(voucherId, voucherDTO)))
                .build();
    }

    @Override
    @PatchMapping("/{voucherId}/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse orderVoucher(@PathVariable("voucherId") String voucherId,
                                       @PathVariable("userId") String userId) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .statusMessage(i18nUtil.getMessage("message.voucher-ordered"))
                .results(List.of(voucherService.order(voucherId, userId)))
                .build();
    }

    @Override
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse findAll(Pageable pageable) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.findAll(pageable)))
                .build();
    }

    @Override
    @GetMapping("/userId/{userId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER') or @authenticationServiceImpl.isCurrentUser(#userId)")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse findAllByUserId(@PathVariable("userId") String userId, Pageable pageable) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.findAllByUserId(userId, pageable)))
                .build();
    }

    @Override
    @GetMapping("/search")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse search(VoucherSearchParamsDto params, Pageable pageable) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.search(params, pageable)))
                .build();
    }

    @Override
    @GetMapping("/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse findById(@PathVariable("voucherId") String voucherId) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.findById(voucherId)))
                .build();
    }

    @Override
    @PatchMapping("/cancel/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse cancelOrder(@PathVariable("voucherId") String voucherId) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.cancelOrder(voucherId)))
                .build();
    }

    @Override
    @PatchMapping("/pay/{voucherId}")
    @ResponseStatus(HttpStatus.OK)
    public RemoteResponse payVoucher(@PathVariable("voucherId") String voucherId) {
        return RemoteResponse.builder()
                .succeeded(true)
                .statusCode(OK.name())
                .results(List.of(voucherService.payVoucher(voucherId)))
                .build();
    }
}
