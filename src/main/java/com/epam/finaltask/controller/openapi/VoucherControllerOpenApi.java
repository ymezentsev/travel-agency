package com.epam.finaltask.controller.openapi;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import com.epam.finaltask.dto.validator.ValueOfEnum;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Voucher Controller", description = "API to work with vouchers")
public interface VoucherControllerOpenApi {
    @Operation(summary = "Create new voucher (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Voucher successfully created",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid voucher data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> createVoucher(@Validated(OnCreate.class) @RequestBody VoucherDTO voucherDTO);

    @Operation(summary = "Update voucher (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher successfully updated",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid voucher data",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Voucher not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> updateVoucher(@PathVariable("voucherId") String voucherId,
                                                 @Validated(OnUpdate.class) @RequestBody VoucherDTO voucherDTO);

    @Operation(summary = "Delete voucher (admin only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher successfully deleted",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Voucher not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> deleteVoucherById(@PathVariable("voucherId") String voucherId);

    @Operation(summary = "Change voucher hot status (admin or manager only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher hot status successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin or manager",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Voucher not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> changeVoucherHotStatus(@PathVariable("voucherId") String voucherId,
                                                          @Valid @RequestBody VoucherDTO voucherDTO);

    @Operation(summary = "Get all vouchers")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAll();

    @Operation(summary = "Get all vouchers by user id",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAllByUserId(@PathVariable("userId") String userId);

    @Operation(summary = "Get vouchers by tour type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid tour type",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAllByTourType(
            @RequestParam("tourType") @ValueOfEnum(enumClass = TourType.class) String tourType);

    @Operation(summary = "Get vouchers by transfer type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid transfer type",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAllByTransferType(
            @RequestParam("transferType") @ValueOfEnum(enumClass = TransferType.class) String transferType);

    @Operation(summary = "Get vouchers by hotel type")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid hotel type",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAllByHotelType(
            @RequestParam("hotelType") @ValueOfEnum(enumClass = HotelType.class) String hotelType);

    @Operation(summary = "Get vouchers by price equal or less")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid price",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> findAllByPrice(
            @RequestParam("price")
            @DecimalMin(value = "0.01", message = "Price must be positive number") double price);

    @Operation(summary = "Order voucher",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully ordered",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Voucher or user not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "409",
                    description = "Voucher already ordered",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> orderVoucher(@PathVariable("voucherId") String voucherId,
                                                @PathVariable("userId") String userId);

    @Operation(summary = "Change voucher's status (admin or manager only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers status successfully changed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad request. Not valid voucher status",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin or manager",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "404",
                    description = "Voucher not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                                       @Validated(OnChangeStatus.class)
                                                       @RequestBody VoucherDTO voucherDTO);

    @Operation(summary = "Get page of vouchers filtered by title, tour type, transfer type, hotel type, voucher status," +
            " hot status, arrival date, eviction date and sorted by chosen parameters")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Unexpected internal error",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Error.class)))
    })
    ResponseEntity<RemoteResponse> search(VoucherSearchParamsDto params, Pageable pageable);
}
