package com.accountmanagement.model;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    CLIENT, ADMIN, ROOT;

    @Override
    public String getAuthority() {
        return this.name();
    }
}
