package com.epam.finaltask.repository.searchcriteria;

public interface SpecificationProviderManager<T> {
    SpecificationProvider<T> getSpecificationProvider(String key);
}
