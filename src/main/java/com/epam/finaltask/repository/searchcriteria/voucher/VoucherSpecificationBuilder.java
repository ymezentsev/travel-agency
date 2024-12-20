package com.epam.finaltask.repository.searchcriteria.voucher;

import com.epam.finaltask.dto.VoucherSearchParametersDto;
import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.searchcriteria.SpecificationBuilder;
import com.epam.finaltask.repository.searchcriteria.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class VoucherSpecificationBuilder implements SpecificationBuilder<Voucher> {
    private final SpecificationProviderManager<Voucher> specificationProviderManager;

    @Override
    public Specification<Voucher> build(VoucherSearchParametersDto searchParameters) {
        Specification<Voucher> spec = Specification.where(null);

        if (searchParameters != null
                && searchParameters.titles() != null
                && searchParameters.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(searchParameters.titles()));
        }

        if (searchParameters != null
                && searchParameters.tourTypes() != null
                && searchParameters.tourTypes().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("tourType")
                    .getSpecification(searchParameters.tourTypes()));
        }

        if (searchParameters != null
                && searchParameters.transferTypes() != null
                && searchParameters.transferTypes().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("transferType")
                    .getSpecification(searchParameters.transferTypes()));
        }

        if (searchParameters != null
                && searchParameters.hotelTypes() != null
                && searchParameters.hotelTypes().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("hotelType")
                    .getSpecification(searchParameters.hotelTypes()));
        }

        if (searchParameters != null
                && searchParameters.voucherStatuses() != null
                && searchParameters.voucherStatuses().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("voucherStatus")
                    .getSpecification(searchParameters.voucherStatuses()));
        }

        if (searchParameters != null && searchParameters.arrivalDate() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("arrivalDate")
                    .getSpecification(new String[]{String.valueOf(searchParameters.arrivalDate())}));
        }

        if (searchParameters != null && searchParameters.minPrice() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("minPrice")
                    .getSpecification(new String[]{String.valueOf(searchParameters.minPrice())}));
        }

        if (searchParameters != null && searchParameters.maxPrice() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("maxPrice")
                    .getSpecification(new String[]{String.valueOf(searchParameters.maxPrice())}));
        }

        if (searchParameters != null && searchParameters.isHot() != null) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("isHot")
                    .getSpecification(new String[]{String.valueOf(searchParameters.isHot())}));
        }
        return spec;
    }
}
