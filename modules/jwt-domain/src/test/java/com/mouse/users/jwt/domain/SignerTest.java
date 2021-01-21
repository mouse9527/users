package com.mouse.users.jwt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

class SignerTest {
    private Signer signer;

    @BeforeEach
    void setUp() throws Exception {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        this.signer = new RSASigner(keyPair.getPrivate());
    }

    @Test
    void should_be_able_to_sign_date() {
        String signature = signer.sign("aaa");

        assertThat(signature).isNotEmpty();
        assertThat(signature).isNotEqualTo("aaa");
    }

    @Test
    void should_be_able_to_encrypt_data() {
        String encrypted = signer.encrypt("aaa");

        assertThat(encrypted).isNotEmpty();
        assertThat(encrypted).isNotEqualTo("aaa");
    }
}
