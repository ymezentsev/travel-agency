package com.epam.finaltask.specification;

import com.epam.finaltask.dto.VoucherSearchParamsDto;
import com.epam.finaltask.model.Voucher;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.epam.finaltask.specification.SpecificationBuilder.*;

@Component
public class VoucherSpecification {
    public Specification<Voucher> build(VoucherSearchParamsDto searchParams) {
        Specification<Voucher> spec = Specification.where(null);

        if (searchParams == null) {
            return spec;
        }

        if (searchParams.getTitles() != null && searchParams.getTitles().length > 0) {
            spec = spec.and(buildLikePredicate("title", searchParams.getTitles()));
        }

        if (searchParams.getTourTypes() != null && searchParams.getTourTypes().length > 0) {
            spec = spec.and(buildInPredicate("tourType",
                    Arrays.stream(searchParams.getTourTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.getTransferTypes() != null && searchParams.getTransferTypes().length > 0) {
            spec = spec.and(buildInPredicate("transferType",
                    Arrays.stream(searchParams.getTransferTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.getHotelTypes() != null && searchParams.getHotelTypes().length > 0) {
            spec = spec.and(buildInPredicate("hotelType",
                    Arrays.stream(searchParams.getHotelTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.getVoucherStatuses() != null && searchParams.getVoucherStatuses().length > 0) {
            spec = spec.and(buildInPredicate("status",
                    Arrays.stream(searchParams.getVoucherStatuses())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.getArrivalDate() != null) {
            spec = spec.and(buildEqualPredicate("arrivalDate", searchParams.getArrivalDate()));
        }

        if (searchParams.getMinPrice() != null) {
            spec = spec.and(buildGreaterThanOrEqualPredicate("price", searchParams.getMinPrice()));
        }

        if (searchParams.getMaxPrice() != null) {
            spec = spec.and(buildLessThanOrEqualPredicate("price", searchParams.getMaxPrice()));
        }

        if (searchParams.getIsHot() != null) {
            spec = spec.and(buildEqualPredicate("isHot", Boolean.parseBoolean(searchParams.getIsHot())));
        }
        return spec;
    }
}
