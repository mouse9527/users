package com.mouse.framework.jwt.domain;


import com.mouse.uses.domain.core.Base64Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;

public class RSASigner implements Signer {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Signature signer;
    private final Cipher cipher;
    private final Integer length;

    public RSASigner(PrivateKey privateKey) {
        try {
            signer = Signature.getInstance("SHA1WithRSA");
            signer.initSign(privateKey);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
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

    @Override
    public Header defaultHeader() {
        return new Header(String.format("RSA%s", length));
    }

    private synchronized byte[] encrypt(byte[] bytes1) {
        try {
            return cipher.doFinal(bytes1);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
