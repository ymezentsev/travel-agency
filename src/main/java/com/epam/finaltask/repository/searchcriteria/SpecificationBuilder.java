package com.epam.finaltask.repository.searchcriteria;

import com.epam.finaltask.dto.VoucherSearchParametersDto;
import org.springframework.data.jpa.domain.Specification;

public interface SpecificationBuilder<T> {
    Specification<T> build(VoucherSearchParametersDto searchParameters);
}
