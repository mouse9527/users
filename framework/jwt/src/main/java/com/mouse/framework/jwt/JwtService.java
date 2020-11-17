package com.mouse.framework.jwt;

public class JwtService {
    private final Signer signer;

    public JwtService(Signer signer) {
        this.signer = signer;
    }

    public Token createToken(Payload payload) {
        Token token = new Token(signer.defaultHeader(), payload);
        token.sign(signer);
        return token;
    }
}
