package com.mouse.framework.jwt;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Header {
    protected static final Header RSA_256 = new Header("RSA256");
    private final String alg = "JWT";
    private final String typ;

    public Header(String typ) {
        this.typ = typ;
    }
}
