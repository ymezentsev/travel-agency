package com.epam.finaltask.specification;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

public class SpecificationBuilder {
    static <T> Specification<T> buildLikePredicate(String field, String[] values) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = Arrays.stream(values)
                    .map(param -> criteriaBuilder.like(root.get(field), "%" + param.strip() + "%"))
                    .toList();
            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    static <T> Specification<T> buildInPredicate(String field, Object[] values) {
        return (root, query, criteriaBuilder) -> root.get(field).in(values);
    }

    static <T> Specification<T> buildEqualPredicate(String field, Object value) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(field), value);
    }

    static <T, V extends Comparable<? super V>> Specification<T> buildLessThanOrEqualPredicate(String field, V value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
    }

    static <T, V extends Comparable<? super V>> Specification<T> buildGreaterThanOrEqualPredicate(String field, V value) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
    }
}
