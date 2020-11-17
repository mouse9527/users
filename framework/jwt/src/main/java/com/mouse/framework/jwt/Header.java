package com.mouse.framework.jwt;

import lombok.Getter;

@Getter
public class Header {
    protected static final Header RSA_256 = new Header("RSA256");
    protected static final Header RSA_1024 = new Header("RSA1024");
    private final String alg = "JWT";
    private final String typ;

    public Header(String typ) {
        this.typ = typ;
    }
}
