package com.mouse.framework.jwt.domain;

import lombok.Getter;

import java.util.Collection;

@Getter
public class Payload {
    private final Long iat;
    private final Long exp;
    private final String jti;
    private final Collection<String> authorities;
    private final Collection<String> scopes;
    private final String name;
    private final String protectedData;

    public Payload(Long iat, Long exp, String jti, Collection<String> authorities, Collection<String> scopes, String name, String protectedData) {
        this.iat = iat;
        this.exp = exp;
        this.jti = jti;
        this.authorities = authorities;
        this.scopes = scopes;
        this.name = name;
        this.protectedData = protectedData;
    }
}
