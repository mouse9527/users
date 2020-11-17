package com.mouse.framework.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Payload {
    private final Long iat;
    private final Long exp;
    private final String iss;
    private final String name;
    private final String typ;
    private final String ciphertext;
}
