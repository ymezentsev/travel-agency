package com.epam.finaltask.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Permission {
    ADMIN_READ("PERM_ADMIN_READ"),
    ADMIN_UPDATE("PERM_ADMIN_UPDATE"),
    ADMIN_CREATE("PERM_ADMIN_CREATE"),
    ADMIN_DELETE("PERM_ADMIN_DELETE"),
    MANAGER_UPDATE("PERM_MANAGER_UPDATE"),
    USER_READ("PERM_USER_READ"),
    USER_UPDATE("PERM_USER_UPDATE"),
    USER_CREATE("PERM_USER_CREATE"),
    USER_DELETE("PERM_USER_DELETE");

    private final String permission;
}
