package com.mouse.users.jwt.domain;

import org.junit.jupiter.api.Test;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;

class FileKeyPairConfigTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void should_be_able_to_create_key_from_file() throws Exception {
        InputStream privateKey = this.getClass().getClassLoader().getResource("privatekey.pem").openStream();
        InputStream publicKey = this.getClass().getClassLoader().getResource("publickey.pem").openStream();

        KeyPairConfig keyPairConfig = new FileKeyPairConfig(privateKey, publicKey);

        assertThat(keyPairConfig.getPrivateKey()).isNotNull();
        assertThat(keyPairConfig.getPublicKey()).isNotNull();
    }
}
