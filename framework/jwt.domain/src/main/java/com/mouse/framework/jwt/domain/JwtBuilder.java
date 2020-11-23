package com.mouse.framework.jwt.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouse.framework.domain.core.Base64Util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtBuilder {
    private final Map<String, Object> payload;
    private final ObjectMapper mapper;

    public JwtBuilder() {
        payload = new HashMap<>();
        mapper = new ObjectMapper();
    }

    public JwtBuilder iat(Long iat) {
        payload.put("iat", iat);
        return this;
    }

    public JwtBuilder exp(Long exp) {
        payload.put("exp", exp);
        return this;
    }

    public JwtBuilder jti(String jti) {
        payload.put("jti", jti);
        return this;
    }

    public JwtBuilder authorities(List<String> authorities) {
        payload.put("authorities", authorities);
        return this;
    }

    public JwtBuilder scopes(List<String> scopes) {
        payload.put("scopes", scopes);
        return this;
    }

    public JwtBuilder name(String username) {
        payload.put("name", username);
        return this;
    }

    public JwtBuilder protectedData(Object protectedData) {
        payload.put("protectedData", protectedData);
        return this;
    }

    public String sign(Signer signer) {
        if (payload.containsKey("protectedData")) {

            Object protectedData = payload.get("protectedData");
            String data;
            if (protectedData instanceof String) {
                data = (String) protectedData;
            } else {
                data = formatAsString(protectedData);
            }
            payload.replace("protectedData", signer.encrypt(data));
        }
        Header header = signer.defaultHeader();
        StringBuilder builder = new StringBuilder();
        builder.append(Base64Util.encodeToString(format(header))).append(".");
        builder.append(Base64Util.encodeToString(format(payload)));

        String signatureData = builder.toString();

        builder.append(".").append(signer.sign(signatureData));
        return builder.toString();
    }

    private String formatAsString(Object data) {
        try {
            return mapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] format(Object data) {
        try {
            return mapper.writeValueAsBytes(data);
        } catch (JsonProcessingException e) {
            return new byte[0];
        }
    }
}
