package com.mouse.framework.jwt;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TokenTest {
    protected static final String MOCK_SIGNATURE = "mock-signature";
    private static final String EXAMPLE_COM = "example.com";
    private static final String LISA = "lisa";
    private static final String VISITOR = "visitor";

    @Test
    void should_be_able_to_sign_correctly() {
        Instant iat = Instant.now();
        Instant exp = iat.plus(10, DAYS);
        Payload<String> payload = Payload.<String>builder()
                .iat(iat.getEpochSecond())
                .exp(exp.getEpochSecond())
                .iss(EXAMPLE_COM)
                .name(LISA)
                .typ(VISITOR)
                .ciphertext("xxxx")
                .build();
        Header header = new Header("JWT", "RSA256");
        Token token = new Token(header, payload);

        Signer signer = mock(Signer.class);
        given(signer.sign(any())).willReturn(MOCK_SIGNATURE);

        token.sign(signer);

        String jwt = token.toString();
        assertThat(jwt).isNotEmpty();
        String[] split = jwt.split("\\.");
        assertThat(split).hasSize(3);
        String jsonHeader = new String(Base64.getDecoder().decode(split[0].getBytes()));
        assertThat(JsonPath.compile("$.alg").<String>read(jsonHeader)).isEqualTo("JWT");
        assertThat(JsonPath.compile("$.typ").<String>read(jsonHeader)).isEqualTo("RSA256");
        String jsonPayload = new String(Base64.getDecoder().decode(split[1].getBytes()));
        assertThat(JsonPath.compile("$.iat").<Integer>read(jsonPayload)).isEqualTo(iat.getEpochSecond());
        assertThat(JsonPath.compile("$.exp").<Integer>read(jsonPayload)).isEqualTo(exp.getEpochSecond());
        assertThat(JsonPath.compile("$.iss").<String>read(jsonPayload)).isEqualTo(EXAMPLE_COM);
        assertThat(JsonPath.compile("$.name").<String>read(jsonPayload)).isEqualTo(LISA);
        assertThat(JsonPath.compile("$.typ").<String>read(jsonPayload)).isEqualTo(VISITOR);
        assertThat(JsonPath.compile("$.ciphertext").<String>read(jsonPayload)).isEqualTo("xxxx");
        assertThat(split[2]).isEqualTo(MOCK_SIGNATURE);
    }
}