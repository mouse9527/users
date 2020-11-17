package com.mouse.framework.jwt;

import lombok.Getter;

@Getter
public class Header {
    private final String alg;
    private final String typ;

    public Header(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }
}
