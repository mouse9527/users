package com.mouse.users.jwt.domain;

import lombok.Generated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

public class FileKeyPairConfig implements KeyPairConfig {
    private static final String PRIVATE = "PRIVATE";
    private static final String PUBLIC = "PUBLIC";
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    @Generated
    public FileKeyPairConfig(InputStream privateKey, InputStream publicKey) {
        try {
            String privateKeyString = privateKeyString(privateKey, PRIVATE);
            String publicKeyString = privateKeyString(publicKey, PUBLIC);
            KeyFactory factory = KeyFactory.getInstance("RSA");
            this.privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString)));
            this.publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString)));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException | IOException e) {
            throw new IllegalArgumentException("Failed to create KeyPairConfig", e);
        }
    }

    private String privateKeyString(InputStream privateKey, String type) throws IOException {
        try (
                InputStreamReader in = new InputStreamReader(privateKey);
                BufferedReader privateKeyReader = new BufferedReader(in)
        ) {
            return privateKeyReader.lines()
                    .collect(Collectors.joining())
                    .replace(String.format("-----BEGIN %s KEY-----", type), "")
                    .replace(String.format("-----END %s KEY-----", type), "")
                    .replaceAll("\\s", "");
        }
    }

    @Override
    public PublicKey getPublicKey() {
        return publicKey;
    }

    @Override
    public PrivateKey getPrivateKey() {
        return privateKey;
    }
}
