package com.mouse.framework.jwt;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
class SignerTest {
    private Signer signer;
    private Signature instance;

    @BeforeEach
    void setUp() throws NoSuchAlgorithmException, InvalidKeyException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.genKeyPair();
        instance = Signature.getInstance("SHA1withRSA");
        instance.initSign(keyPair.getPrivate());

        signer = new RSASigner(keyPair.getPrivate());
    }

    @Test
    void should_be_able_to_sign_payload() throws SignatureException {
        Payload<String> payload = Payload.<String>builder()
                .iat(Instant.now().getEpochSecond())
                .exp(Instant.now().getEpochSecond())
                .iss("mouse.com")
                .name("admin")
                .typ("logged")
                .ciphertext("xxx")
                .build();

        String jwt = signer.sign(payload);

        String expected = createExpected(payload);

        assertThat(jwt.split("\\.")[2]).isEqualTo(expected);
    }

    private String createExpected(Payload<String> payload) throws SignatureException {
        String encodeHeader = Base64Util.encodeToString(JSONUtil.serialize(new Header("RSA1024")));
        String encodePayload = Base64Util.encodeToString(JSONUtil.serialize(payload));
        instance.update(String.format("%s.%s", encodeHeader, encodePayload).getBytes(StandardCharsets.UTF_8));
        return Base64Util.encodeToString(instance.sign());
    }
}