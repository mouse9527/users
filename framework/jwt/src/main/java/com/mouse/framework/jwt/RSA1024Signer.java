package com.mouse.framework.jwt;

import java.security.*;

public class RSA1024Signer implements Signer {

    private final Signature signer;

    public RSA1024Signer(KeyPair keyPair) {
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
        return Header.RSA_1024;
    }
}
