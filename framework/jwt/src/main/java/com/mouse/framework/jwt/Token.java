package com.mouse.framework.jwt;

import java.nio.charset.StandardCharsets;

public class Token {
    private final String header;
    private final String payload;

    public Token(Header header, Payload<?> payload) {
        this.header = Base64Util.encodeToString(JsonUtil.formatToBytes(header));
        this.payload = Base64Util.encodeToString(JsonUtil.formatToBytes(payload));
    }

    public String sign(Signer signer) {
        byte[] data = String.format("%s.%s", header, payload).getBytes(StandardCharsets.UTF_8);
        return String.format("%s.%s.%s", header, payload, signer.sign(data));
    }

    public String sign(String signature) {
        return String.format("%s.%s.%s", header, payload, signature);
    }
}
