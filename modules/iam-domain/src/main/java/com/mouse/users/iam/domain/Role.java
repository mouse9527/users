package com.mouse.users.iam.domain;

public class Role {
    private String id;
    private String name;
    private AuthoritiesSet authorities;

    public Role(String id, String name, AuthoritiesSet authorities) {
        this.id = id;
        this.name = name;
        this.authorities = authorities;
    }

    public AuthoritiesSet getAuthorities() {
        return authorities;
    }
}
