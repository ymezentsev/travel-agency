package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.OnChangeStatus;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import com.epam.finaltask.dto.validator.DateOrder;
import com.epam.finaltask.dto.validator.ValueOfEnum;
import com.epam.finaltask.model.enums.HotelType;
import com.epam.finaltask.model.enums.TourType;
import com.epam.finaltask.model.enums.TransferType;
import com.epam.finaltask.model.enums.VoucherStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@DateOrder(groups = {OnCreate.class, OnUpdate.class}, firstDate = "arrivalDate", secondDate = "evictionDate",
        message = "{validation.dates-wrong-order}")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for voucher")
public class VoucherDTO {
    @Schema(description = "Voucher id", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private String id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.title-required}")
    @Length(max = 255, message = "{validation.title-validation-message}")
    @Schema(description = "Voucher title", example = "Summer escape")
    private String title;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.description-required}")
    @Length(max = 2000, message = "{validation.description-validation-message}")
    @Schema(description = "Voucher description", example = "Enjoy a summer escape")
    private String description;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.price-required}")
    @DecimalMin(groups = {OnCreate.class, OnUpdate.class}, value = "0.01",
            message = "{validation.price-validation-message}")
    @Schema(description = "Voucher price", example = "299.99")
    private BigDecimal price;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.tour-type-required}")
    @ValueOfEnum(enumClass = TourType.class, message = "{validation.enum-validation-message}")
    @Schema(description = "Tour type", example = "ADVENTURE")
    private String tourType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.transfer-type-required}")
    @ValueOfEnum(enumClass = TransferType.class, message = "{validation.enum-validation-message}")
    @Schema(description = "Transfer type", example = "PLANE")
    private String transferType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.hotel-type-required}")
    @ValueOfEnum(enumClass = HotelType.class, message = "{validation.enum-validation-message}")
    @Schema(description = "Hotel type", example = "FIVE_STARS")
    private String hotelType;

    @NotNull(groups = {OnChangeStatus.class, OnUpdate.class}, message = "{validation.voucher-status-required}")
    @ValueOfEnum(enumClass = VoucherStatus.class, message = "{validation.enum-validation-message}")
    @Schema(description = "Voucher status", example = "REGISTERED")
    private String status;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.arrival-date-required}")
    @FutureOrPresent(message = "{validation.arrival-date-validation-message}")
    @Schema(description = "Arrival date", example = "2025-11-23")
    private LocalDate arrivalDate;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "{validation.eviction-date-required}")
    @Future(message = "{validation.eviction-date-validation-message}")
    @Schema(description = "Eviction date", example = "2025-11-30")
    private LocalDate evictionDate;

    @Schema(description = "Id of the user who reserved the voucher", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private UUID userId;

    @Schema(description = "Username of the user who reserved the voucher", example = "user")
    private String username;

    @Schema(description = "Is this hot voucher", defaultValue = "false")
    private String isHot;
}
