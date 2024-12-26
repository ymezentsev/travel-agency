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

import java.time.LocalDate;
import java.util.UUID;

@Data
@DateOrder(groups = {OnCreate.class, OnUpdate.class}, firstDate = "arrivalDate", secondDate = "evictionDate",
        message = "The eviction date must be after or equal to arrival date")
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dto for voucher")
public class VoucherDTO {
    @Schema(description = "Voucher id", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private String id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Title is required")
    @Length(max = 255, message = "Max size for title is 255 characters")
    @Schema(description = "Voucher title", example = "Summer escape")
    private String title;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Description is required")
    @Length(max = 2000, message = "Max size for description is 2000 characters")
    @Schema(description = "Voucher description", example = "Enjoy a summer escape")
    private String description;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Price is required")
    @DecimalMin(groups = {OnCreate.class, OnUpdate.class}, value = "0.01",
            message = "Price must be positive number")
    @Schema(description = "Voucher price", example = "299.99")
    private double price;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Tour type is required")
    @ValueOfEnum(enumClass = TourType.class)
    @Schema(description = "Tour type", example = "ADVENTURE")
    private String tourType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Transfer type is required")
    @ValueOfEnum(enumClass = TransferType.class)
    @Schema(description = "Transfer type", example = "PLANE")
    private String transferType;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Hotel type is required")
    @ValueOfEnum(enumClass = HotelType.class)
    @Schema(description = "Hotel type", example = "FIVE_STARS")
    private String hotelType;

    @NotNull(groups = {OnChangeStatus.class, OnUpdate.class}, message = "Voucher status is required")
    @ValueOfEnum(enumClass = VoucherStatus.class)
    @Schema(description = "Voucher status", example = "REGISTERED")
    private String status;

    //todo for tests remove check FutureOrPresent
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Arrival date is required")
    @FutureOrPresent(message = "Arrival date must be future or present")
    @Schema(description = "Arrival date", example = "2025-11-23")
    private LocalDate arrivalDate;

    //todo for tests remove check Future
    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Eviction date is required")
    @Future(message = "Eviction date must be future")
    @Schema(description = "Eviction date", example = "2025-11-30")
    private LocalDate evictionDate;

    @Schema(description = "Id of the user who reserved the voucher", example = "f3e02ce0-365d-4c03-90a1-98f00cf6d3d1")
    private UUID userId;

    @Schema(description = "Username of the user who reserved the voucher", example = "user")
    private String username;

    @Schema(description = "Is this hot voucher", defaultValue = "false")
    private String isHot;
}
