package com.mouse.framework.jwt;

import java.util.HashMap;
import java.util.Map;

public class Payload<T> extends HashMap<String, Object> {
    public Payload(Map<String, Object> payload) {
        this.putAll(payload);
    }

    public static <R> PayloadBuilder<R> builder() {
        return new PayloadBuilder<>();
    }

    static class PayloadBuilder<T> {
        private final Map<String, Object> payload;

        public PayloadBuilder() {
            this.payload = new HashMap<>();
        }

        public PayloadBuilder<T> iat(Long iat) {
            payload.put("iat", iat);
            return this;
        }

        public PayloadBuilder<T> exp(Long exp) {
            payload.put("exp", exp);
            return this;
        }

        public PayloadBuilder<T> iss(String iss) {
            payload.put("iss", iss);
            return this;
        }

        public PayloadBuilder<T> name(String name) {
            payload.put("name", name);
            return this;
        }

        public PayloadBuilder<T> typ(String typ) {
            payload.put("typ", typ);
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
