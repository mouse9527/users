package com.mouse.framework.jwt.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Objects;

public class Payload extends HashMap<String, Object> {

    public Payload(Instant iat, Instant exp, String ciphertext) {
        super();
        validate(iat, exp, ciphertext);
        put("iat", iat.getEpochSecond());
        put("exp", exp.getEpochSecond());
        put("ciphertext", ciphertext);
    }

    private void validate(Instant iat, Instant exp, String ciphertext) {
        if (Objects.isNull(iat)) {
            throw new IllegalArgumentException("Illegal argument [iat]");
        }
        if (Objects.isNull(exp) || exp.isBefore(iat)) {
            throw new IllegalArgumentException("Illegal argument [exp]");
        }
        if (StringUtils.isEmpty(ciphertext)) {
            throw new IllegalArgumentException("Illegal argument [ciphertext]");
        }
    }

    public Instant getIat() {
        return Instant.ofEpochSecond((Long) get("iat"));
    }

    public Instant getExp() {
        return Instant.ofEpochSecond((Long) get("exp"));
    }

    public String getCiphertext() {
        return (String) get("ciphertext");
    }
}
