package com.mouse.framework.jwt.domain;

import com.mouse.framework.common.domain.Base64Util;

import java.nio.charset.StandardCharsets;
import java.security.*;

public class RsaSigner implements Signer {

    private final Signature signer;

    public RsaSigner(PrivateKey privateKey) {
        try {
            signer = Signature.getInstance("SHA1WithRSA");
            signer.initSign(privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sign(String data) {
        try {
            signer.update(data.getBytes(StandardCharsets.UTF_8));
            return Base64Util.encodeToString(signer.sign());
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
    }
}
