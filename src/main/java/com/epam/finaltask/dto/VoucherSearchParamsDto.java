package com.epam.finaltask.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record VoucherSearchParamsDto(String[] titles,
                                     String[] tourTypes, String[] transferTypes,
                                     String[] hotelTypes, String[] voucherStatuses,
                                     LocalDate arrivalDate, Boolean isHot,
                                     Double minPrice, Double maxPrice) {
}
