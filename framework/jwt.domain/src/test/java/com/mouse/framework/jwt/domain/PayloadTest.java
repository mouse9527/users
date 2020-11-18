package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class PayloadTest {

    @Test
    void should_be_able_to_build_correctly() {
        Instant iat = Instant.parse("2020-11-18T00:00:00Z");
        Instant exp = Instant.parse("2020-11-18T00:00:00Z");
        Payload<String> payload = new Payload<>(iat, exp, "xxxx");

        assertThat(payload.getIat()).isEqualTo(iat);
        assertThat(payload.getExp()).isEqualTo(exp);
        assertThat(payload.getCiphertext()).isEqualTo("xxxx");
    }
}