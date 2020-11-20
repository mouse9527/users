package com.mouse.framework.jwt.domain;

import com.mouse.framework.common.domain.Base64Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RSASigner implements Signer {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Signature signer;
    private final Cipher cipher;

    public RSASigner(PrivateKey privateKey) {
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
        return Base64Util.encodeToString(sign(data.getBytes(CHARSET)));
    }

    private synchronized byte[] sign(byte[] data) {
        byte[] bytes;
        try {
            signer.update(data);
            bytes = signer.sign();
        } catch (SignatureException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    @Override
    public String encrypt(String data) {
        return Base64Util.encodeToString(encrypt(data.getBytes(CHARSET)));
    }

    private synchronized byte[] encrypt(byte[] bytes1) {
        byte[] bytes;
        try {
            bytes = cipher.doFinal(bytes1);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return bytes;







    }


}
