package com.mouse.framework.jwt;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;

public class RSASigner implements Signer {
    private final Signature signer;
    private final Header defaultHeader;

    public RSASigner(KeyPair keyPair) {
        defaultHeader = new Header(String.format("RSA%s", ((RSAPrivateKey) keyPair.getPrivate()).getModulus().bitLength()));
        try {
            signer = Signature.getInstance("SHA1withRSA");
            signer.initSign(keyPair.getPrivate());
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
}
