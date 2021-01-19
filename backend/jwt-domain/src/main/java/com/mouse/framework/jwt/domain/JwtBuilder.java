package com.mouse.framework.jwt.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mouse.uses.domain.core.Base64Util;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class JwtBuilder {
    private final ObjectMapper mapper;
    private Long iat;
    private Long exp;
    private String jti;
    private Collection<String> authorities;
    private Collection<String> scopes;
    private String name;
    private Object protectedData;
    private String encrypt;

    public JwtBuilder() {
        mapper = new ObjectMapper();
    }

    public JwtBuilder iat(Long iat) {
        this.iat = iat;
        return this;
    }

    public JwtBuilder exp(Long exp) {
        this.exp = exp;
        return this;
    }

    public JwtBuilder jti(String jti) {
        this.jti = jti;
        return this;
    }

    public JwtBuilder authorities(Collection<String> authorities) {
        this.authorities = authorities;
        return this;
    }

    public JwtBuilder scopes(List<String> scopes) {
        this.scopes = scopes;
        return this;
    }

    public JwtBuilder name(String username) {
        this.name = username;
        return this;
    }

    public JwtBuilder protectedData(Object protectedData) {
        this.protectedData = protectedData;
        return this;
    }

    public String sign(Signer signer) {
        if (hasProtectedData()) {
            encryptData(signer);
        }
        Header header = signer.defaultHeader();
        StringBuilder builder = new StringBuilder();
        builder.append(Base64Util.encodeToString(format(header))).append(".");
        builder.append(Base64Util.encodeToString(format(getPayload())));

        String signatureData = builder.toString();

        builder.append(".").append(signer.sign(signatureData));
        return builder.toString();
    }

    private Payload getPayload() {
        return new Payload(
                iat,
                exp,
                jti,
                authorities,
                scopes,
                name,
                encrypt
        );
    }

    private void encryptData(Signer signer) {
        String data;
        if (protectedData instanceof String) {
            data = (String) protectedData;
        } else {
            data = formatAsString(protectedData);
        }
        encrypt = signer.encrypt(data);
    }

    private boolean hasProtectedData() {
        return Objects.nonNull(protectedData);
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
