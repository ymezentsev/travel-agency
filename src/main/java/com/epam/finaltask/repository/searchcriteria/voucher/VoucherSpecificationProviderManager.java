package com.epam.finaltask.repository.searchcriteria.voucher;

import com.epam.finaltask.model.Voucher;
import com.epam.finaltask.repository.searchcriteria.SpecificationProvider;
import com.epam.finaltask.repository.searchcriteria.SpecificationProviderManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class VoucherSpecificationProviderManager implements SpecificationProviderManager<Voucher> {
    private final List<SpecificationProvider<Voucher>> requestSpecificationProviders;

    @Override
    public SpecificationProvider<Voucher> getSpecificationProvider(String key) {
        return requestSpecificationProviders.stream()
                .filter(provider -> provider.getKey().equals(key))
                .findFirst()
                .orElseThrow(() ->
                        new RuntimeException("Can't find correct specification provider for key " + key));
    }
}