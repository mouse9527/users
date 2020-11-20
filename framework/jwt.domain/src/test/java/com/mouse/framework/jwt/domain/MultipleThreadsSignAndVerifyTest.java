package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import static org.assertj.core.api.Assertions.assertThat;

class MultipleThreadsSignAndVerifyTest {
    private static final int COUNT = 10;
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
    void should_be_able_to_sign_in_multiple_threads() throws InterruptedException {
        Map<String, String> signatures = new ConcurrentHashMap<>();
        CountDownLatch signingCountDown = new CountDownLatch(COUNT);

        for (int i = 0; i < COUNT; i++) {
            final String data = String.valueOf(i);
            new Thread(() -> {
                String signature = signer.sign(data);
                signatures.put(data, signature);
                signingCountDown.countDown();
            }).start();
        }
        signingCountDown.await();

        List<Boolean> verifies = Collections.synchronizedList(new ArrayList<>());
        CountDownLatch verifyingCountDownLatch = new CountDownLatch(COUNT);
        signatures.forEach((data, signature) -> {
            new Thread(() -> {
                boolean verify = verifier.verify(signature, data);
                verifies.add(verify);
                verifyingCountDownLatch.countDown();
            }).start();
        });
        verifyingCountDownLatch.await();

        assertThat(verifies).hasSize(COUNT);
        assertThat(verifies).allMatch(Boolean::booleanValue);
    }
}
