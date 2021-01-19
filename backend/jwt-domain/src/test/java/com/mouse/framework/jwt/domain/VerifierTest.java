package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class VerifierTest {
    private Signer signer;
    private Verifier verifier;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        this.signer = new RSASigner(keyPair.getPrivate());
        this.verifier = new RSAVerifier(keyPair.getPublic());
    }

    @Test
    void should_be_able_to_verify_signature() {
        String signature = signer.sign("aaa");

        assertThat(verifier.verify(signature, "aaa")).isTrue();
        assertThat(verifier.verify(signature, "aa")).isFalse();
    }

    @Test
    void should_be_able_to_decrypt_encrypted_date() {
        String encrypted = signer.encrypt("aaa");

        assertThat(verifier.decrypt(encrypted)).isEqualTo("aaa");
    }
}
