package com.mouse.framework.jwt;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;

public class RSASigner implements Signer {
    private final Signature signer;
    private final Header defaultHeader;

    public RSASigner(PrivateKey privateKey) {
        defaultHeader = new Header(String.format("RSA%s", ((RSAPrivateKey) privateKey).getModulus().bitLength()));
        try {
            signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sign(byte[] context) {
        try {
            signer.update(context);
            return Base64Util.encodeToString(signer.sign());
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Header defaultHeader() {
        return defaultHeader;
    }

    @Override
    public String sign(Payload<?> payload) {
        Token token = new Token(defaultHeader, payload);
        return token.sign(this);
    }
}
