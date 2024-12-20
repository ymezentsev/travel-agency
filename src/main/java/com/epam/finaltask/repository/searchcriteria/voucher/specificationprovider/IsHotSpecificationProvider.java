package com.epam.finaltask.repository.searchcriteria.voucher.specificationprovider;

import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.searchcriteria.SpecificationProvider;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class IsHotSpecificationProvider implements SpecificationProvider<Voucher> {
    @Override
    public String getKey() {
        return "isHot";
    }

    public Specification<Voucher> getSpecification(String[] params) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isHot"), Boolean.parseBoolean(params[0]));
    }
}
