package com.mouse.users.jwt.domain;

import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.stream.Collectors;

public class FileKeyPairConfig implements KeyPairConfig {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public FileKeyPairConfig(Path privateKey, Path publicKey) throws Exception {
        String privateKeyString = Files.lines(privateKey)
                .collect(Collectors.joining())
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");

        String publicKeyString = Files.lines(publicKey)
                .collect(Collectors.joining())
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

        KeyFactory factory = KeyFactory.getInstance("RSA");
        this.privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString)));
        this.publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyString)));
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
