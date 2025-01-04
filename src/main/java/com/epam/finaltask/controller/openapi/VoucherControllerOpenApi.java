package com.epam.finaltask.controller.openapi;

import com.epam.finaltask.dto.RemoteResponse;
import com.epam.finaltask.dto.VoucherDTO;
import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

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
    RemoteResponse createVoucher(@Validated(OnCreate.class) @RequestBody VoucherDTO voucherDTO);

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
    RemoteResponse updateVoucher(@PathVariable("voucherId") String voucherId,
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
                    responseCode = "405",
                    description = "Voucher cannot be deleted",
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
    RemoteResponse deleteVoucherById(@PathVariable("voucherId") String voucherId);

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
                    responseCode = "405",
                    description = "'HOT' change is allowed only for vouchers with the 'AVAILABLE' status",
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
    RemoteResponse changeVoucherHotStatus(@PathVariable("voucherId") String voucherId,
                                          @Valid @RequestBody VoucherDTO voucherDTO);

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
                    description = "Status cannot be set for a voucher",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "405",
                    description = "'HOT' change is allowed only for vouchers with the 'AVAILABLE' status",
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
    RemoteResponse changeVoucherStatus(@PathVariable("voucherId") String voucherId,
                                       @Validated(OnChangeStatus.class)
                                       @RequestBody VoucherDTO voucherDTO);

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
                    responseCode = "405",
                    description = "Only vouchers with status 'AVAILABLE' can be ordered",
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
    RemoteResponse orderVoucher(@PathVariable("voucherId") String voucherId,
                                @PathVariable("userId") String userId);

    @Operation(summary = "Get page of all vouchers")
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
    RemoteResponse findAll(Pageable pageable);

    @Operation(summary = "Get page of all vouchers by user id (admin, manager or current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Vouchers successfully obtained",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not admin, manager or current user",
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
    RemoteResponse findAllByUserId(@PathVariable("userId") String userId, Pageable pageable);

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
    RemoteResponse search(VoucherSearchParamsDto params, Pageable pageable);

    @Operation(summary = "Find voucher by id")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher successfully obtained",
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
    RemoteResponse findById(@PathVariable("voucherId") String voucherId);

    @Operation(summary = "Cancel order of voucher by id (current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher's order successfully canceled",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not a current user",
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
                    responseCode = "405",
                    description = "Only ordered vouchers with status 'REGISTERED' can be cancelled",
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
    RemoteResponse cancelOrder(@PathVariable("voucherId") String voucherId);

    @Operation(summary = "Pay order of voucher by id (current user only)",
            security = @SecurityRequirement(name = "Bearer Authentication"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Voucher's order successfully paid",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RemoteResponse.class))),
            @ApiResponse(
                    responseCode = "403",
                    description = "Access denied. User not a current user",
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
                    responseCode = "405",
                    description = "Only ordered vouchers with status 'REGISTERED' can be paid",
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
    RemoteResponse payVoucher(@PathVariable("voucherId") String voucherId);
}
