package com.epam.finaltask.controller;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.exception.StatusCodes;
import com.epam.finaltask.service.VoucherService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collections;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    private static final String STATUS_MESSAGE_OK = "OK";
    private static final String STATUS_MESSAGE_CREATED = "Voucher is successfully created";
    private static final String STATUS_MESSAGE_UPDATED = "Voucher is successfully updated";
    private static final String STATUS_MESSAGE_DELETED = "Voucher with Id %s has been deleted";
    private static final String STATUS_MESSAGE_CHANGED = "Voucher status is successfully changed";

    @PostMapping("/create")
    public ResponseEntity<RemoteResponse> createVoucher(@Valid @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_CREATED, List.of(voucherService.create(voucherDTO))),
                HttpStatus.CREATED);
    }

    @PatchMapping("/change/{voucherId}")
    public ResponseEntity<RemoteResponse> updateVoucher(@PathVariable("voucherId") String voucherId,
                                                        @Valid @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_UPDATED, List.of(voucherService.update(voucherId, voucherDTO))),
                HttpStatus.OK);
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<RemoteResponse> deleteVoucherById(@PathVariable("voucherId") String voucherId) {
        voucherService.delete(voucherId);
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                String.format(STATUS_MESSAGE_DELETED, voucherId), Collections.EMPTY_LIST),
                HttpStatus.OK);
    }

    @PatchMapping("/{voucherId}")
    public ResponseEntity<RemoteResponse> changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                                              @Valid @RequestBody VoucherDTO voucherDTO) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_CHANGED, List.of(voucherService.changeHotStatus(voucherId, voucherDTO))),
                HttpStatus.OK);
    }

        @GetMapping
        public ResponseEntity<RemoteResponse> findAll() {
            return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                    STATUS_MESSAGE_OK, voucherService.findAll()),
                    HttpStatus.OK);
        }
/*    @GetMapping
    public ModelAndView findAll() {
        ModelAndView result = new ModelAndView("index");
      //  result.addObject(ATTRIBUTE_USERNAME, authentication.getName());
        result.addObject("remoteResponse",
                new RemoteResponse(true, StatusCodes.OK.name(),
                        STATUS_MESSAGE_OK, voucherService.findAll()));
        return result;
    }*/

    @GetMapping("/{userId}")
    public ResponseEntity<RemoteResponse> findAllByUserId(@PathVariable("userId") String userId) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByUserId(userId)),
                HttpStatus.OK);
    }

    @GetMapping("/by-tourType")
    public ResponseEntity<RemoteResponse> findAllByTourType(@RequestParam("tourType") String tourType) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByTourType(tourType)),
                HttpStatus.OK);
    }

    @GetMapping("/by-transferType")
    public ResponseEntity<RemoteResponse> findAllByTransferType(@RequestParam("transferType") String transferType) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByTransferType(transferType)),
                HttpStatus.OK);
    }

    @GetMapping("/by-hotelType")
    public ResponseEntity<RemoteResponse> findAllByHotelType(@RequestParam("hotelType") String hotelType) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByHotelType(hotelType)),
                HttpStatus.OK);
    }

    @GetMapping("/by-price")
    public ResponseEntity<RemoteResponse> findAllByPrice(@RequestParam("price") String price) {
        return new ResponseEntity<>(new RemoteResponse(true, StatusCodes.OK.name(),
                STATUS_MESSAGE_OK, voucherService.findAllByPrice(price)),
                HttpStatus.OK);
    }
}
