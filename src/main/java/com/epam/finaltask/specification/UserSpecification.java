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

        if (searchParams.usernames() != null && searchParams.usernames().length > 0) {
            spec = spec.and(buildLikePredicate("username", searchParams.usernames()));
        }

        if (searchParams.roles() != null && searchParams.roles().length > 0) {
            spec = spec.and(buildInPredicate("role",
                    Arrays.stream(searchParams.roles())
                            .map(String::toUpperCase)
                            .toArray()));
        }

        if (searchParams.phoneNumbers() != null && searchParams.phoneNumbers().length > 0) {
            spec = spec.and(buildLikePredicate("phoneNumber", searchParams.phoneNumbers()));
        }

        if (searchParams.emails() != null && searchParams.emails().length > 0) {
            spec = spec.and(buildLikePredicate("email", searchParams.emails()));
        }

        if (searchParams.isUnlocked() != null) {
            spec = spec.and(buildEqualPredicate("accountStatus", searchParams.isUnlocked()));
        }
        return spec;
    }
}
