package com.mouse.framework.jwt.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouse.framework.domain.core.Base64Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtBuilder<T> {
    private final Map<String, Object> payload;
    private final ObjectMapper mapper;

    public JwtBuilder() {
        payload = new HashMap<>();
        mapper = new ObjectMapper();
    }

    public JwtBuilder<T> iat(Long iat) {
        payload.put("iat", iat);
        return this;
    }

    public JwtBuilder<T> exp(Long exp) {
        payload.put("exp", exp);
        return this;
    }

    public JwtBuilder<T> jti(String jti) {
        payload.put("jti", jti);
        return this;
    }

    public JwtBuilder<T> authorities(List<String> authorities) {
        payload.put("authorities", authorities);
        return this;
    }

    public JwtBuilder<T> scopes(List<String> scopes) {
        payload.put("scopes", scopes);
        return this;
    }

    public JwtBuilder<T> name(String username) {
        payload.put("name", username);
        return this;
    }

    public JwtBuilder<T> protectedData(T protectedData) {
        payload.put("protectedData", protectedData);
        return this;
    }

    public String sign(Signer signer) {
        if (payload.containsKey("protectedData")) {
            payload.replace("protectedData", signer.encrypt(payload.get("protectedData").toString()));
        }
        Header header = signer.defaultHeader();
        StringBuilder builder = new StringBuilder();
        builder.append(Base64Util.encodeToString(format(header))).append(".");
        builder.append(Base64Util.encodeToString(format(payload)));

        String signatureData = builder.toString();

        builder.append(".").append(signer.sign(signatureData));
        return builder.toString();
    }

    private byte[] format(Object data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            return new byte[0];
        }
    }
}
