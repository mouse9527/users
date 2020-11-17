package com.mouse.framework.jwt;

import lombok.Getter;

@Getter
public class Header {
    protected static final Header RSA256 = new Header("JWT", "RSA256");
    private final String alg;
    private final String typ;

    public Header(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }
}
