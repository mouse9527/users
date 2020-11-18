package com.mouse.framework.jwt.domain;

import com.jayway.jsonpath.JsonPath;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Base64;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

@Log4j2
@Disabled
class TokenTest {
    protected static final String MOCK_SIGNATURE = "mock-signature";
    private static final String EXAMPLE_COM = "example.com";
    private static final String LISA = "lisa";
    private static final String SIGN_DATA = "eyJhbGciOiJKV1QiLCJ0eXAiOiJSU0EyNTYifQ==.eyJjaXBoZXJ0ZXh0IjoieHh4eCIsImlzcyI6ImV4YW1wbGUuY29tIiwibmFtZSI6Imxpc2EiLCJ0eXAiOiJ2aXNpdG9yIiwiZXhwIjoxNjA2NTIxNjAwLCJpYXQiOjE2MDU2NTc2MDB9";

    @Test
    void should_be_able_to_sign_correctly() {
        Instant iat = Instant.parse("2020-11-18T00:00:00Z");
        Instant exp = iat.plus(10, DAYS);
        Payload<String> payload = Payload.<String>builder()
                .iat(iat)
                .exp(exp)
                .iss(EXAMPLE_COM)
                .ciphertext("xxxx")
                .build();

        Token token = new Token(Header.RSA_256, payload);

        assertThat(new String(token.getSignData())).isEqualTo(SIGN_DATA);
        String jwt = token.sign(MOCK_SIGNATURE);
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
        assertThat(JsonPath.compile("$.ciphertext").<String>read(jsonPayload)).isEqualTo("xxxx");
        assertThat(split[2]).isEqualTo(MOCK_SIGNATURE);
    }
}