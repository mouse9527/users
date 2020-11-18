package com.mouse.users.iam.domain;

import com.mouse.framework.jwt.RSATokenSigner;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

public class TokenServiceTest {
    @Test
    void should_be_able_to_allocate_token() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();

        RSATokenSigner rsaSigner = new RSATokenSigner(keyPair.getPrivate());
    }
}
