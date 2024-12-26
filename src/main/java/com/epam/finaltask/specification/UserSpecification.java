package com.epam.finaltask.specification;

import com.epam.finaltask.dto.UserSearchParamsDto;
import com.epam.finaltask.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.epam.finaltask.specification.SpecificationBuilder.*;

@Component
public class UserSpecification {
    public Specification<User> build(UserSearchParamsDto searchParams) {
        Specification<User> spec = Specification.where(null);

        if (searchParams == null) {
            return spec;
        }

        if (searchParams.getUsernames() != null && searchParams.getUsernames().length > 0) {
            spec = spec.and(buildLikePredicate("username", searchParams.getUsernames()));
        }

        if (searchParams.getRoles() != null && searchParams.getRoles().length > 0) {
            spec = spec.and(buildInPredicate("role",
                    Arrays.stream(searchParams.getRoles())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.getPhoneNumbers() != null && searchParams.getPhoneNumbers().length > 0) {
            spec = spec.and(buildLikePredicate("phoneNumber", searchParams.getPhoneNumbers()));
        }

        if (searchParams.getEmails() != null && searchParams.getEmails().length > 0) {
            spec = spec.and(buildLikePredicate("email", searchParams.getEmails()));
        }

        if (searchParams.getIsUnlocked() != null) {
            spec = spec.and(buildEqualPredicate("accountStatus", Boolean.parseBoolean(searchParams.getIsUnlocked())));
        }
        return spec;
    }
}
