package com.mouse.framework.jwt.domain;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class Payload<T> extends HashMap<String, Object> {
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

        public Payload<T> build() {
            return new Payload<>(payload);
        }
    }
}
