package com.raven.api.model.enums;

public enum RoleName {
    ROLE_ADMIN, ROLE_USER;

    public static RoleName getDefault() {
        return ROLE_USER;
    }
}
