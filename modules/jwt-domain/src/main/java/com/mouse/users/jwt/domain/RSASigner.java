package com.mouse.users.jwt.domain;


import com.mouse.uses.domain.core.Base64Util;
import lombok.Generated;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.interfaces.RSAPrivateKey;

public class RSASigner implements Signer {

    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private final Signature signer;
    private final Cipher cipher;
    private final Integer length;

    public RSASigner(PrivateKey privateKey) throws Exception {
        signer = Signature.getInstance("SHA1WithRSA");
        signer.initSign(privateKey);
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        length = ((RSAPrivateKey) privateKey).getModulus().bitLength();
    }

    @Override
    public String sign(String data) {
        return Base64Util.encodeToString(sign(data.getBytes(CHARSET)));
    }

    @Generated
    private synchronized byte[] sign(byte[] data) {
        try {
            signer.update(data);
            return signer.sign();
        } catch (SignatureException e) {
            // do nothing
            throw new RuntimeException(e);
        }
    }

    @Override
    public String encrypt(String data) {
        return Base64Util.encodeToString(encrypt(data.getBytes(CHARSET)));
    }

    @Override
    public Header defaultHeader() {
        return new Header(String.format("RSA%s", length));
    }

    @Generated
    private synchronized byte[] encrypt(byte[] bytes1) {
        try {
            return cipher.doFinal(bytes1);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
