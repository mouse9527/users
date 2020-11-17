package com.mouse.framework.jwt;

public class Token {
    private final String header;
    private final String payload;
    private String signature;

    public Token(Header header, Payload payload) {
        this.header = Base64Util.encodeToString(Serializer.serialize(header));
        byte[] serialize = Serializer.serialize(payload);
        this.payload = Base64Util.encodeToString(serialize);
    }

    public void sign(Signer signer) {
        String signText = String.format("%s.%s", header, payload);
        this.signature = signer.sign(Base64Util.encode(signText));
    }

    @Override
    public String toString() {
        return String.format("%s.%s.%s", header, payload, signature);
    }
}
