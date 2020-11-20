package com.mouse.framework.jwt.domain;

import java.time.Instant;
import java.util.List;

public class JwtBuilder<T> {
    public JwtBuilder() {
    }

    public JwtBuilder<T> iat(Instant iat) {
        return null;
    }

    public JwtBuilder<T> exp(Instant exp) {
        return null;
    }

    public JwtBuilder<T> jti(String jti) {
        return null;
    }

    public JwtBuilder<T> authorities(List<String> authorities) {
        return null;
    }

    public JwtBuilder<T> scope(List<String> scopes) {
        return null;
    }

    public JwtBuilder<T> username(String username) {
        return null;
    }

    public JwtBuilder<T> protectedData(T protectedData) {
        return null;
    }

    public String sign(Signer signer) {
        return null;
    }
}
