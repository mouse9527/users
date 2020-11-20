package com.mouse.framework.jwt.domain;

import com.mouse.framework.common.domain.Base64Util;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class RSAVerifier implements Verifier {
    private final Signature signature;

    public RSAVerifier(PublicKey publicKey) {
        try {
            signature = Signature.getInstance("SHA1WithRSA");
            signature.initVerify(publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String signature, String data) {
        try {
            this.signature.update(data.getBytes(StandardCharsets.UTF_8));
            return this.signature.verify(Base64Util.decode(signature));
        } catch (SignatureException e) {
            return false;
        }
    }
}
