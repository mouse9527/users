package com.mouse.framework.jwt.domain;


import com.mouse.uses.domain.core.Base64Util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.charset.StandardCharsets;
import java.security.*;

public class RSAVerifier implements Verifier {
    private final Signature verifier;
    private final Cipher cipher;

    public RSAVerifier(PublicKey publicKey) {
        try {
            verifier = Signature.getInstance("SHA1WithRSA");
            verifier.initVerify(publicKey);
            cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
        } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean verify(String signature, String data) {
        return verify(Base64Util.decode(signature), data.getBytes(StandardCharsets.UTF_8));
    }

    private synchronized boolean verify(byte[] signature, byte[] data) {
        try {
            verifier.update(data);
            return verifier.verify(signature);
        } catch (SignatureException e) {
            return false;
        }
    }

    @Override
    public String decrypt(String encrypted) {
        byte[] bytes = Base64Util.decode(encrypted);
        return new String(decrypt(bytes));
    }

    private synchronized byte[] decrypt(byte[] bytes) {
        try {
            return cipher.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
    }
}
