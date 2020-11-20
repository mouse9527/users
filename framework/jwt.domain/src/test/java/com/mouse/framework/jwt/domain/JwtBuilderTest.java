package com.mouse.framework.jwt.domain;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static org.mockito.Mockito.mock;

public class JwtBuilderTest {
    @Test
    @Disabled // TODO: not implemented
    void should_be_able_to_create_correctly() {
        Signer signer = mock(Signer.class);

        String jwt = new JwtBuilder<String>()
                .iat(Instant.now())
                .exp(Instant.now())
                .jti("mock-jti")
                .authorities(Collections.singletonList("admin"))
                .scope(Collections.singletonList("all"))
                .username("lisa su")
                .protectedData("xxx")
                .sign(signer);

    }
}
