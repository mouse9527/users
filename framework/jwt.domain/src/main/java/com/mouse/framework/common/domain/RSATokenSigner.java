package com.mouse.framework.common.domain;

import com.mouse.framework.users.core.Header;
import com.mouse.framework.users.core.Payload;
import com.mouse.framework.users.core.Token;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;

public class RSATokenSigner implements TokenSigner {
    private final Signature signer;
    private final Header defaultHeader;

    public RSATokenSigner(PrivateKey privateKey) {
        defaultHeader = new Header(String.format("RSA%s", ((RSAPrivateKey) privateKey).getModulus().bitLength()));
        try {
            signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sign(Payload<?> payload) {
        Token token = new Token(defaultHeader, payload);
        return token.sign(sign(token.getSignData()));
    }

    public String sign(byte[] context) {
        try {
            signer.update(context);
            return Base64Util.encodeToString(signer.sign());
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
