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

        if (searchParams.titles() != null && searchParams.titles().length > 0) {
            spec = spec.and(buildLikePredicate("title", searchParams.titles()));
        }

        if (searchParams.tourTypes() != null && searchParams.tourTypes().length > 0) {
            spec = spec.and(buildInPredicate("tourType",
                    Arrays.stream(searchParams.tourTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.transferTypes() != null && searchParams.transferTypes().length > 0) {
            spec = spec.and(buildInPredicate("transferType",
                    Arrays.stream(searchParams.transferTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.hotelTypes() != null && searchParams.hotelTypes().length > 0) {
            spec = spec.and(buildInPredicate("hotelType",
                    Arrays.stream(searchParams.hotelTypes())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.voucherStatuses() != null && searchParams.voucherStatuses().length > 0) {
            spec = spec.and(buildInPredicate("status",
                    Arrays.stream(searchParams.voucherStatuses())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.arrivalDate() != null) {
            spec = spec.and(buildEqualPredicate("arrivalDate", searchParams.arrivalDate()));
        }

        if (searchParams.minPrice() != null) {
            spec = spec.and(buildGreaterThanOrEqualPredicate("price", searchParams.minPrice()));
        }

        if (searchParams.maxPrice() != null) {
            spec = spec.and(buildLessThanOrEqualPredicate("price", searchParams.maxPrice()));
        }

        if (searchParams.isHot() != null) {
            spec = spec.and(buildEqualPredicate("isHot", searchParams.isHot()));
        }
        return spec;
    }
}
