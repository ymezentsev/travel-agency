package com.epam.finaltask.controller;

import com.epam.finaltask.controller.openapi.VoucherControllerOpenApi;
import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import com.epam.finaltask.dto.validator.ValueOfEnum;
import com.epam.finaltask.model.HotelType;
import com.epam.finaltask.model.TourType;
import com.epam.finaltask.model.TransferType;
import com.epam.finaltask.service.VoucherService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.epam.finaltask.exception.StatusCodes.OK;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/vouchers")
public class VoucherController implements VoucherControllerOpenApi {
    private final VoucherService voucherService;

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_CREATED = "Voucher is successfully created";
    private static final String STATUS_MESSAGE_UPDATED = "Voucher is successfully updated";
    private static final String STATUS_MESSAGE_DELETED = "Voucher with Id %s has been deleted";
    private static final String STATUS_MESSAGE_CHANGED = "Voucher status is successfully changed";
    private static final String STATUS_MESSAGE_ORDERED = "Voucher is successfully ordered";

    @Override
    @PostMapping("/create")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> createVoucher(@Validated(OnCreate.class) @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CREATED, List.of(voucherService.create(voucherDTO))),
                HttpStatus.CREATED);
    }

    @Override
    @PatchMapping("/change/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> updateVoucher(@PathVariable("voucherId") String voucherId,
                                                        @Validated(OnUpdate.class) @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_UPDATED, List.of(voucherService.update(voucherId, voucherDTO))),
                HttpStatus.OK);
    }

    @Override
    @DeleteMapping("/{voucherId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> deleteVoucherById(@PathVariable("voucherId") String voucherId) {
        voucherService.delete(voucherId);
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                String.format(STATUS_MESSAGE_DELETED, voucherId), Collections.EMPTY_LIST),
                HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{voucherId}")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<RemoteResponse> changeVoucherHotStatus(@PathVariable("voucherId") String voucherId,
                                                                 @Valid @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CHANGED, List.of(voucherService.changeHotStatus(voucherId, voucherDTO))),
                HttpStatus.OK);
    }

    @Override
    @GetMapping
    public ResponseEntity<RemoteResponse> findAll() {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAll()),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/{userId}")
    public ResponseEntity<RemoteResponse> findAllByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByUserId(userId)),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/by-tourType")
    public ResponseEntity<RemoteResponse> findAllByTourType(
            @RequestParam("tourType") @ValueOfEnum(enumClass = TourType.class) String tourType) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByTourType(tourType)),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/by-transferType")
    public ResponseEntity<RemoteResponse> findAllByTransferType(
            @RequestParam("transferType") @ValueOfEnum(enumClass = TransferType.class) String transferType) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByTransferType(transferType)),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/by-hotelType")
    public ResponseEntity<RemoteResponse> findAllByHotelType(
            @RequestParam("hotelType") @ValueOfEnum(enumClass = HotelType.class) String hotelType) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByHotelType(hotelType)),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/by-price")
    public ResponseEntity<RemoteResponse> findAllByPrice(
            @RequestParam("price")
            @DecimalMin(value = "0.01", message = "Price must be positive number") double price) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByPrice(price)),
                HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{voucherId}/{userId}")
    public ResponseEntity<RemoteResponse> orderVoucher(@PathVariable("voucherId") String voucherId,
                                                       @PathVariable("userId") String userId) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_ORDERED, List.of(voucherService.order(voucherId, userId))),
                HttpStatus.OK);
    }

    @Override
    @PatchMapping("/{voucherId}/status")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    public ResponseEntity<RemoteResponse> changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                                              @Validated(OnChangeStatus.class)
                                                              @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_CHANGED, List.of(voucherService.changeStatus(voucherId, voucherDTO))),
                HttpStatus.OK);
    }

    @Override
    @GetMapping("/search")
    public ResponseEntity<RemoteResponse> search(VoucherSearchParamsDto params, Pageable pageable) {
        return new ResponseEntity<>(new RemoteResponse(true, OK.name(),
                STATUS_MESSAGE_OK, List.of(voucherService.search(params, pageable))),
                HttpStatus.OK);
    }
}
