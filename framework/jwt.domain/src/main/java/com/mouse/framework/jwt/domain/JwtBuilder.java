package com.mouse.framework.jwt.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouse.framework.domain.core.Base64Util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtBuilder {
    public static final String PROTECTED_DATA = "protectedData";
    public static final String NAME = "name";
    public static final String SCOPES = "scopes";
    public static final String AUTHORITIES = "authorities";
    public static final String JTI = "jti";
    public static final String EXP = "exp";
    public static final String IAT = "iat";
    private final Map<String, Object> payload;
    private final ObjectMapper mapper;

    public JwtBuilder() {
        payload = new HashMap<>();
        mapper = new ObjectMapper();
    }

    public JwtBuilder iat(Long iat) {
        payload.put(IAT, iat);
        return this;
    }

    public JwtBuilder exp(Long exp) {
        payload.put(EXP, exp);
        return this;
    }

    public JwtBuilder jti(String jti) {
        payload.put(JTI, jti);
        return this;
    }

    public JwtBuilder authorities(Collection<String> authorities) {
        payload.put(AUTHORITIES, authorities);
        return this;
    }

    public JwtBuilder scopes(List<String> scopes) {
        payload.put(SCOPES, scopes);
        return this;
    }

    public JwtBuilder name(String username) {
        payload.put(NAME, username);
        return this;
    }

    public JwtBuilder protectedData(Object protectedData) {
        payload.put(PROTECTED_DATA, protectedData);
        return this;
    }

    public String sign(Signer signer) {
        if (hasProtectedData()) {
            encryptData(signer);
        }
        Header header = signer.defaultHeader();
        StringBuilder builder = new StringBuilder();
        builder.append(Base64Util.encodeToString(format(header))).append(".");
        builder.append(Base64Util.encodeToString(format(payload)));

        String signatureData = builder.toString();

        builder.append(".").append(signer.sign(signatureData));
        return builder.toString();
    }

    private void encryptData(Signer signer) {
        Object protectedData = payload.get(PROTECTED_DATA);
        String data;
        if (protectedData instanceof String) {
            data = (String) protectedData;
        } else {
            data = formatAsString(protectedData);
        }
        payload.replace(PROTECTED_DATA, signer.encrypt(data));
    }

    private boolean hasProtectedData() {
        return payload.containsKey(PROTECTED_DATA);
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
