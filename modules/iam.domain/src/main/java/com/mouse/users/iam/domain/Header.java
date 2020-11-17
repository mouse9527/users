package com.mouse.users.iam.domain;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Header {
    private final String alg;
    private final String typ;

    public Header(String alg, String typ) {
        this.alg = alg;
        this.typ = typ;
    }

    @Override
    public String toString() {
        String jsonHeader = String.format("{\"alg\":\"%s\",\"typ\":\"%s\"}", alg, typ);
        return Base64.getEncoder().encodeToString(jsonHeader.getBytes(StandardCharsets.UTF_8));
    }
}
