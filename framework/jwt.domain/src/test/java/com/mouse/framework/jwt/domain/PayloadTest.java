package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PayloadTest {

    @Test
    void should_be_able_to_build_correctly() {
        Instant iat = Instant.parse("2020-11-18T00:00:00Z");
        Instant exp = Instant.parse("2020-11-18T00:00:00Z");
        Payload<String> build = Payload.<String>builder()
                .iat(iat)
                .exp(exp)
                .ciphertext("xxxx")
                .build();

        assertThat(build.getIat()).isEqualTo(iat);
        assertThat(build.getExp()).isEqualTo(exp);
        assertThat(build.getCiphertext()).isEqualTo("xxxx");
    }
}