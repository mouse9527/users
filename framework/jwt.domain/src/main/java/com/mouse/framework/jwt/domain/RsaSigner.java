package com.mouse.framework.jwt.domain;

import com.mouse.framework.common.domain.Base64Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RsaSigner implements Signer {

    private final Signature signer;
    private final Cipher cipher;

    public RsaSigner(PrivateKey privateKey) {
        try {
            signer = Signature.getInstance("SHA1WithRSA");
            signer.initSign(privateKey);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sign(String data) {
        byte[] bytes;
        try {
            signer.update(data.getBytes(StandardCharsets.UTF_8));
            bytes = signer.sign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return Base64Util.encodeToString(bytes);
    }

    @Override
    public String encrypt(String data) {
        byte[] bytes;
        try {
            bytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return Base64Util.encodeToString(bytes);
    }
}
