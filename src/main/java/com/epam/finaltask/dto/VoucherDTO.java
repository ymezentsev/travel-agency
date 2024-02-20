package com.epam.finaltask.dto;

import com.epam.finaltask.dto.group.OnChange;
import com.epam.finaltask.dto.group.OnCreate;
import com.epam.finaltask.dto.group.OnUpdate;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "Voucher Data Transfer Object")
public class VoucherDTO {
    @Schema(description = "Voucher id")
    private String id;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Title is required field")
    @Length(max = 100, message = "Max voucher title size is 50 characters")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "Title must contain only characters and numbers")
    @Schema(description = "Voucher title", required = true)
    private String title;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Description is required field")
    @Length(max = 500, message = "Max vendor name size is 50 characters")
    @Schema(description = "Voucher description", required = true)
    private String description;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Price is required field")
    @Schema(description = "Voucher price", required = true)
    private double price;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Tour type is required field")
    @Schema(description = "Voucher tour type", required = true)
    private String tourType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Transfer type is required field")
    @Schema(description = "Voucher transfer type", required = true)
    private String transferType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Hotel type is required field")
    @Schema(description = "Voucher hotel", required = true)
    private String hotelType;

    @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "Status is required field")
    @Schema(description = "Voucher status", required = true)
    private String status;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Arrival date is required field")
    @Schema(description = "Voucher arrival date", required = true)
    private LocalDate arrivalDate;

    @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "Eviction date is required field")
    @Schema(description = "Voucher eviction date", required = true)
    private LocalDate evictionDate;

    @Schema(description = "Voucher's user")
    private UUID userId;

    @Schema(description = "Voucher is hot", defaultValue = "false")
    private String isHot;
}
