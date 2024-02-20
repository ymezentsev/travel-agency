package com.epam.finaltask.controller;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.group.OnChange;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.exception.StatusCodes;
import com.epam.finaltask.service.VoucherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/vouchers")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Voucher", description = "Voucher func")
public class VoucherController {

    private final VoucherService voucherService;

    @Operation(summary = "Get all vouchers")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting all existed vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping
    public ResponseEntity<RemoteResponse> findAll() {
        List<VoucherDTO> result = voucherService.findAll();
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting all existed vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Get all vouchers by userId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting user's vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<RemoteResponse> findAllByUserId(
            @Parameter(description = "All user's vouchers", required = true) @PathVariable String userId
    ) {
        List<VoucherDTO> result = voucherService.findAllByUserId(userId);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting user's vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Get all vouchers by tourType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting user's vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping("/tour/{tourType}")
    public ResponseEntity<RemoteResponse> findAllByTourType(
            @Parameter(description = "All vouchers by tourType", required = true) @PathVariable String tourType
    ) {
        List<VoucherDTO> result = voucherService.findAllByTourType(tourType);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting user's vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Get all vouchers by transferType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting user's vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping("/transfer/{transferType}")
    public ResponseEntity<RemoteResponse> findAllByTransferType(
            @Parameter(description = "All vouchers by transferType", required = true) @PathVariable String transferType
    ) {
        List<VoucherDTO> result = voucherService.findAllByTransferType(transferType);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting user's vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Get all vouchers by hotelType")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting user's vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping("/hotel/{hotelType}")
    public ResponseEntity<RemoteResponse> findAllByHotelType(
            @Parameter(description = "All vouchers by transferType", required = true) @PathVariable String hotelType
    ) {
        List<VoucherDTO> result = voucherService.findAllByHotelType(hotelType);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting user's vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Get all vouchers by price")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The request for getting user's vouchers is successful", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @GetMapping("/price/{price}")
    public ResponseEntity<RemoteResponse> findAllByPrice(
            @Parameter(description = "All vouchers by price", required = true) @PathVariable String price
    ) {
        List<VoucherDTO> result = voucherService.findAllByPrice(price);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(), "The request for getting user's vouchers is successful", result);
        return ResponseEntity.ok()
                .body(remoteResponse);
    }

    @Operation(summary = "Create a new voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error. For example, title must contain only characters and numbers", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Voucher has already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    public ResponseEntity<RemoteResponse> createVoucher(
            @Parameter(description = "Voucher data to create", required = true) @Validated(OnCreate.class) @RequestBody VoucherDTO voucherDTO) {
        log.info("Start voucher creation with title={}", voucherDTO.getTitle());

        VoucherDTO createdVoucherDto = voucherService.create(voucherDTO);

        log.info("Voucher with title={} created successfully", voucherDTO.getTitle());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "Voucher is successfully created", List.of(createdVoucherDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(successfulResponse);
    }

    @Operation(summary = "Order a voucher")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful creation", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error. For example, title must contain only characters and numbers", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Voucher has already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PostMapping("/{orderId}/{userId}")
    public ResponseEntity<RemoteResponse> orderVoucher(
            @Parameter(description = "Voucher data to update", required = true) @PathVariable String orderId, @PathVariable String userId) {
        log.info("Start voucher ordering with id={}", orderId);

        VoucherDTO orderedVoucherDto = voucherService.order(orderId, userId);

        log.info("Voucher with id={} created successfully", orderId);

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "Voucher is successfully ordered", List.of(orderedVoucherDto));
        return ResponseEntity.ok().body(successfulResponse);
    }

    @Operation(summary = "Update voucher data")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful update", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error.", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "409", description = "Voucher with this title has already exist", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PatchMapping("/change/{id}")
    public ResponseEntity<RemoteResponse> updateUser(
            @Parameter(description = "Voucher data to update", required = true) @PathVariable String id, @Validated @RequestBody VoucherDTO voucherDTO) {
        log.info("Start updating voucher={} data", id);

        VoucherDTO updatedVoucherDto = voucherService.update(id, voucherDTO);

        log.info("Voucher with title={} updated successfully", updatedVoucherDto.getTitle());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "Voucher is successfully updated", List.of(updatedVoucherDto));
        return ResponseEntity.ok().body(successfulResponse);
    }

    @Operation(summary = "Delete specific voucher given its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voucher has been successfully deleted", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "404", description = "Voucher for a given id has not found", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = RemoteResponse.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<RemoteResponse> deleteById(@Parameter(description = "Voucher Identification", required = true)
                                                     @PathVariable("id") String id) {
        voucherService.delete(id);
        RemoteResponse remoteResponse = RemoteResponse.create(true, StatusCodes.OK.name(),
                String.format("Voucher with Id %s has been deleted", id), null);
        return ResponseEntity.ok().body(remoteResponse);
    }

    @Operation(summary = "Change voucher status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successful change", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "400", description = "Validation Error.", content = @Content(schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected internal error", content = @Content(schema = @Schema(implementation = Error.class)))
    })
    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PatchMapping("/{id}")
    public ResponseEntity<RemoteResponse> changeVoucherStatus(
            @Parameter(description = "Voucher status to change", required = true) @PathVariable String id, @Validated(OnChange.class) @RequestBody VoucherDTO voucherDTO) {
        log.info("Start changing voucher={} status", id);

        VoucherDTO updatedVoucherDto = voucherService.changeHotStatus(id, voucherDTO);

        log.info("Voucher with title={} updated successfully", updatedVoucherDto.getTitle());

        RemoteResponse successfulResponse = RemoteResponse.create(true, OK.name(), "Voucher status is successfully changed", List.of(updatedVoucherDto));
        return ResponseEntity.ok().body(successfulResponse);
    }

}
