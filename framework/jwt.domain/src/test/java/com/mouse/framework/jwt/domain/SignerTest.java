package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

class SignerTest {

    @Test
    void should_be_able_to_sign_date() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        Signer signer = new RsaSigner(keyPair.getPrivate());

        String signature = signer.sign("aaa");

        assertThat(signature).isNotEmpty();
        assertThat(signature).isNotEqualTo("aaa");
    }
}