package com.epam.finaltask.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("system");
    }
}
