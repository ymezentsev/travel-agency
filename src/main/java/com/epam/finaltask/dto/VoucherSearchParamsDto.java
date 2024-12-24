package com.epam.finaltask.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoucherSearchParamsDto {
    private String[] titles;
    private String[] tourTypes;
    private String[] transferTypes;
    private String[] hotelTypes;
    private String[] voucherStatuses;
    private LocalDate arrivalDate;
    private String isHot;
    private Double minPrice;
    private Double maxPrice;
}
