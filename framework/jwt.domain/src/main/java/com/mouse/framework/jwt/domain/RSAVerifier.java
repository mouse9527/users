package com.mouse.framework.jwt.domain;

import com.mouse.framework.common.domain.Base64Util;

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
        byte[] bytes = data.getBytes(StandardCharsets.UTF_8);
        synchronized (verifier) {
            try {
                verifier.update(bytes);
                return verifier.verify(Base64Util.decode(signature));
            } catch (SignatureException e) {
                return false;
            }
        }
    }

    @Override
    public String decrypt(String encrypted) {
        byte[] bytes = Base64Util.decode(encrypted);
        byte[] decrypted;
        try {
            decrypted = cipher.doFinal(bytes);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException(e);
        }
        return new String(decrypted);
    }
}
