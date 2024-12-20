package com.epam.finaltask.repository.searchcriteria.voucher.specificationprovider;

import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.searchcriteria.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ArrivalDateSpecificationProvider implements SpecificationProvider<Voucher> {
    @Override
    public String getKey() {
        return "arrivalDate";
    }

    public Specification<Voucher> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("arrivalDate"), LocalDate.parse(params[0]));
    }
}
