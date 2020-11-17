package com.mouse.framework.jwt;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

class TokenTest {
    private static final String EXAMPLE_COM = "example.com";
    private static final String LISA = "lisa";
    private static final String VISITOR = "visitor";

    @Test
    void should_be_able_to_sign_correctly() {
        Instant iat = Instant.now();
        Instant exp = iat.plus(10, DAYS);
        Payload payload = Payload.builder()
                .iat(iat.getEpochSecond())
                .exp(exp.getEpochSecond())
                .iss(EXAMPLE_COM)
                .name(LISA)
                .typ(VISITOR)
                .ciphertext("xxxx")
                .build();
        Header header = new Header("JWT", "RSA256");
        Token token = new Token(header, payload);

        Signer signer = context -> Base64.getEncoder().encodeToString(context);

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
        String signText = String.format("%s.%s", Base64Util.encodeToString(Serializer.serialize(header)), Base64Util.encodeToString(Serializer.serialize(payload)));
        String expected = Base64.getEncoder().encodeToString(Base64Util.encode(signText));
        assertThat(split[2]).isEqualTo(expected);
    }
}