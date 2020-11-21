package com.mouse.framework.jwt.domain;

import com.jayway.jsonpath.JsonPath;
import com.mouse.framework.domain.core.Base64Util;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JwtBuilderTest {

    private static final String MOCK_JTI = "mock-jti";
    private static final String MOCK_SIGNATURE = "mock-signature";

    @Test
    void should_be_able_to_create_correctly() {
        Signer signer = mock(Signer.class);
        given(signer.encrypt("xxx")).willReturn("yyy");
        given(signer.sign(any())).willReturn(MOCK_SIGNATURE);
        given(signer.defaultHeader()).willReturn(new Header("RSA1024"));
        Instant iat = Instant.now();
        Instant exp = Instant.now();

        String jwt = new JwtBuilder<String>()
                .iat(iat.getEpochSecond())
                .exp(exp.getEpochSecond())
                .jti(MOCK_JTI)
                .authorities(Collections.singletonList("admin"))
                .scopes(Collections.singletonList("all"))
                .name("lisa su")
                .protectedData("xxx")
                .sign(signer);

        String[] split = jwt.split("\\.");
        assertThat(split).hasSize(3);
        String header = Base64Util.decodeToString(split[0]);
        assertThat(JsonPath.compile("$.alg").<String>read(header)).isEqualTo("RSA1024");
        assertThat(JsonPath.compile("$.typ").<String>read(header)).isEqualTo("JWT");
        String payload = Base64Util.decodeToString(split[1]);
        assertThat(JsonPath.compile("$.iat").<Integer>read(payload)).isEqualTo(iat.getEpochSecond());
        assertThat(JsonPath.compile("$.exp").<Integer>read(payload)).isEqualTo(exp.getEpochSecond());
        assertThat(JsonPath.compile("$.jti").<String>read(payload)).isEqualTo(MOCK_JTI);
        assertThat(JsonPath.compile("$.authorities[0]").<String>read(payload)).isEqualTo("admin");
        assertThat(JsonPath.compile("$.scopes[0]").<String>read(payload)).isEqualTo("all");
        assertThat(JsonPath.compile("$.name").<String>read(payload)).isEqualTo("lisa su");
        assertThat(JsonPath.compile("$.protectedData").<String>read(payload)).isEqualTo("yyy");
        assertThat(split[2]).isEqualTo(MOCK_SIGNATURE);
    }
}
