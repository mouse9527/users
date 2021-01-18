package com.mouse.framework.jwt.domain;

import lombok.Getter;

@Getter
public class Header {
    private final String alg;
    private final String typ;

    public Header(String alg) {
        this.typ = "JWT";
        this.alg = alg;
    }
}
