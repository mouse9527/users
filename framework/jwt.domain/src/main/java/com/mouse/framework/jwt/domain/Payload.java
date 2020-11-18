package com.mouse.framework.jwt.domain;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Payload extends HashMap<String, Object> {

    public Payload(Instant iat, Instant exp, String ciphertext) {
        super();
        if (Objects.isNull(iat)) {
            throw new IllegalArgumentException("Illegal argument [iat]");
        }
        if (Objects.isNull(exp) || exp.isBefore(iat)) {
            throw new IllegalArgumentException("Illegal argument [exp]");
        }
        if (StringUtils.isEmpty(ciphertext)) {
            throw new IllegalArgumentException("Illegal argument [ciphertext]");
        }
        put("iat", iat.getEpochSecond());
        put("exp", exp.getEpochSecond());
        put("ciphertext", ciphertext);
    }

    public Payload(Map<String, Object> payload) {
        this.putAll(payload);
    }

    public static <R> PayloadBuilder<R> builder() {
        return new PayloadBuilder<>();
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

    public static class PayloadBuilder<T> {
        private final Map<String, Object> payload;

        PayloadBuilder() {
            this.payload = new HashMap<>();
        }

        public PayloadBuilder<T> iat(Instant iat) {
            payload.put("iat", iat.getEpochSecond());
            return this;
        }

        public PayloadBuilder<T> exp(Instant exp) {
            payload.put("exp", exp.getEpochSecond());
            return this;
        }

        public PayloadBuilder<T> ciphertext(T t) {
            payload.put("ciphertext", t);
            return this;
        }

        public Payload build() {
            return new Payload(payload);
        }
    }
}
