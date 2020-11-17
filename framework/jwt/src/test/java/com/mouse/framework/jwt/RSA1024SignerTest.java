package com.mouse.framework.jwt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class RSA1024SignerTest {
    private static final byte[] DATA = "mock-context".getBytes(StandardCharsets.UTF_8);
    private static final String SIGNATURE = "D5DhYDWCA2winslR6Ou9oUT06XTtLkOfFPsuw+qtvGpv7wl9GEFKV5U0D8SS9lkco4KZXheaJsS3p2m28XT5ufoum3Mb0qyTCBgTJ9LdS2hCNC+sbw+CTkz31uCwV+MUD5PRwzaSw240F6VVSa8xR50ejbBtbF1EFUQMCtsSs9c=";
    private static final String PRIVATE_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAJApDN5COZRbLRRDhYVfllx+DSiOrCpOEwAIGK01Du7U97xMt6Z7rTgNC6OI4pSi5lkqQX6J+5uPhSJrdIbPuXSWnnzne818X7JMRFl6nKix3EvaUlsLBg2I0qPeFe4kozWWIrsIlEGwboERpJfC8nGFkOclZfSImyIc7dmgCYCZAgMBAAECgYB1Mlo6yn4DBQil46rF3grgUL2jzjFbBzPdH7foAC2OrmZkl73pw7vzyG9A2MTTb4dyz66FD+oT8l6at974xiEO1SlIgm+Wqxwhyqa1hGA7kD/rkb9UJA5H9meyoNUe6jZ7ZQrzzP+UduDArR3MxFqg6oiYWOg6lCgNE1JQMyFHMQJBAM9w7TqgHIwvvmSeVEr/eF3S6Oiw6GuEfN7/iRcrZV5zNn2d3E+4UP1sFP/uSyrZ+rIKq29+wY4/Qx1xshdHEW0CQQCx5/k4jOehBdfXLMhHWzBkqgFhz5+SLhvZKMaVRe8nv7yeFrCRB4/IRl7Md2sISvKpe96h7SjgUDVfsQegqVxdAkB9wwTPqgokoz1WWvbXkRI0L42spLQDmwrpVxqRxWlrXYYHmhDa8++F9GS21I0VqZlnHzjYG4zU5F2YGDnUCu3xAkEApgFgkz0f8igCpL0b0UGs4XGlQoC7VGsD/1nGG4obRArs1NM1RM86glXvpXkU/bL5xd5Y+t3Sk6UkeLs289q03QJBAKJ/bY+pV5tNo/kXBYDP+AK3VgvjR2NO7dk0PCeHzxR/rXQJKuCfdNm3hq7Ox+ARUqQknSR7x/9/0ImoTY/1uAM=";
    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCQKQzeQjmUWy0UQ4WFX5Zcfg0ojqwqThMACBitNQ7u1Pe8TLeme604DQujiOKUouZZKkF+ifubj4Uia3SGz7l0lp5853vNfF+yTERZepyosdxL2lJbCwYNiNKj3hXuJKM1liK7CJRBsG6BEaSXwvJxhZDnJWX0iJsiHO3ZoAmAmQIDAQAB";

    @Test
    void should_be_able_to_sign_correctly() throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.getDecoder().decode(PUBLIC_KEY)));
        PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(PRIVATE_KEY)));

        Signer signer = new RSA1024Signer(new KeyPair(publicKey, privateKey));

        assertThat(signer.defaultHeader()).isEqualTo(Header.RSA_1024);
        assertThat(signer.sign(DATA)).isEqualTo(SIGNATURE);
    }

    @Test
    void should_be_able_to_test() throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.genKeyPair();
        log.info("Private Key: {}", Base64Util.encodeToString(keyPair.getPrivate().getEncoded()));
        log.info("Public Key: {}", Base64Util.encodeToString(keyPair.getPublic().getEncoded()));

        Signature instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(keyPair.getPrivate());
        instance.update(DATA);
        byte[] sign = instance.sign();
        log.info(Base64Util.encodeToString(sign));
    }
}