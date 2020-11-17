package com.mouse.framework.jwt;

public class JwtService {
    private final Signer signer;

    public JwtService(Signer signer) {
        this.signer = signer;
    }

    public String createToken(Payload<?> payload) {
        Token token = new Token(signer.defaultHeader(), payload);
        return token.sign(signer);
    }
}
