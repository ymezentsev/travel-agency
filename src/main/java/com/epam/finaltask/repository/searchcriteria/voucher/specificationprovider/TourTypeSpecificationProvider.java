package com.epam.finaltask.repository.searchcriteria.voucher.specificationprovider;

import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.searchcriteria.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class TourTypeSpecificationProvider implements SpecificationProvider<Voucher> {
    @Override
    public String getKey() {
        return "tourType";
    }

    public Specification<Voucher> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) -> root
                .get("tourType")
                .in(Arrays.stream(params)
                        .map(String::toUpperCase)
                        .toArray());
    }
}
