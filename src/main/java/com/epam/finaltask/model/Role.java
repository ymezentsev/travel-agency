package com.epam.finaltask.model;

//import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public enum Role {
    ADMIN,
    MANAGER,
    USER;

    private Set<Permission> permissions;

/*    public List<SimpleGrantedAuthority> getAuthorities() {
        return null;
    }*/
}
