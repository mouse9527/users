package com.mouse.framework.jwt;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JwtServiceTest {

    @Test
    void should_be_able_to_create_token_correctly() {
        Signer mockSigner = mock(Signer.class);
        given(mockSigner.defaultHeader()).willReturn(Header.RSA_256);
        given(mockSigner.sign(any())).willReturn("mock-signature");
        JwtService jwtService = new JwtService(mockSigner);
        Payload<String> payload = Payload.<String>builder()
                .iat(Instant.now().getEpochSecond())
                .exp(Instant.now().getEpochSecond())
                .iss("mouse.com")
                .name("admin")
                .typ("logged")
                .ciphertext("xxx")
                .build();

        String jwt = jwtService.createToken(payload);

        Token expected = new Token(Header.RSA_256, payload);
        assertThat(jwt).isEqualTo(expected.sign(mockSigner));
    }
}