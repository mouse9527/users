package com.mouse.users.jwt.domain;

import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileKeyPairConfigTest {

    @SuppressWarnings("ConstantConditions")
    @Test
    void should_be_able_to_create_key_from_file() {
        Path privateKey = Path.of(this.getClass().getClassLoader().getResource("privatekey.pem").getPath());
        Path publicKey = Path.of(this.getClass().getClassLoader().getResource("publickey.pem").getPath());

        KeyPairConfig keyPairConfig = new FileKeyPairConfig(privateKey, publicKey);

        assertThat(keyPairConfig.getPrivateKey()).isNotNull();
        assertThat(keyPairConfig.getPublicKey()).isNotNull();
    }
}
