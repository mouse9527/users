package com.mouse.framework.jwt;

public class Token {
    private final String header;
    private final String payload;

    public Token(Header header, Payload<?> payload) {
        this.header = Base64Util.encodeToString(Serializer.serialize(header));
        this.payload = Base64Util.encodeToString(Serializer.serialize(payload));
    }

    public String sign(Signer signer) {
        String signature = signer.sign(Base64Util.encode(String.format("%s.%s", header, payload)));
        return String.format("%s.%s.%s", header, payload, signature);
    }
}
