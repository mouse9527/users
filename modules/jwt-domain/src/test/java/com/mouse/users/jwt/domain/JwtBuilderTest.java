package com.mouse.users.jwt.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.mouse.uses.domain.core.Base64Util;
import com.mouse.framework.test.JsonObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class JwtBuilderTest {

    private static final String MOCK_JTI = "mock-jti";
    private static final String MOCK_SIGNATURE = "mock-signature";
    private Signer signer;

    @BeforeEach
    void setUp() {
        signer = mock(Signer.class);
        given(signer.encrypt("xxx")).willReturn("yyy");
        given(signer.sign(any())).willReturn(MOCK_SIGNATURE);
        given(signer.defaultHeader()).willReturn(new Header("RSA1024"));
    }

    @Test
    void should_be_able_to_create_correctly() {
        Instant iat = Instant.now();
        Instant exp = Instant.now();

        String jwt = new JwtBuilder()
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

    @Test
    void should_be_able_to_sign_jwt_without_protected_data() {
        long iat = Instant.now().getEpochSecond();
        long exp = Instant.now().getEpochSecond();
        String jwt = new JwtBuilder()
                .iat(iat)
                .exp(exp)
                .jti(MOCK_JTI)
                .authorities(Collections.singletonList("admin"))
                .sign(signer);

        JsonObject header = new JsonObject(Base64Util.decodeToString(jwt.split("\\.")[0]));
        assertThat(header.strVal("$.alg")).isEqualTo("RSA1024");
        assertThat(header.strVal("$.typ")).isEqualTo("JWT");
        JsonObject payload = new JsonObject(Base64Util.decodeToString(jwt.split("\\.")[1]));
        assertThat(payload.intVal("$.iat")).isEqualTo(iat);
        assertThat(payload.intVal("$.exp")).isEqualTo(exp);
        assertThat(payload.strVal("$.authorities[0]")).isEqualTo("admin");
        assertThat(payload.has("$.protectedData")).isFalse();
        assertThat(payload.has("$.name")).isFalse();
        assertThat(payload.has("$.scopes")).isFalse();
        assertThat(jwt.split("\\.")[2]).isEqualTo(MOCK_SIGNATURE);
    }

    @Test
    void should_be_able_to_sign_protected_date_with_object() throws Exception {
        Map<String, String> data = Collections.singletonMap("id", "mock-id");
        String jsonData = new ObjectMapper().writeValueAsString(data);
        given(signer.encrypt(jsonData)).willReturn(jsonData);

        String jwt = new JwtBuilder()
                .protectedData(data)
                .sign(signer);

        JsonObject payload = new JsonObject(Base64Util.decodeToString(jwt.split("\\.")[1]));
        assertThat(new JsonObject(payload.strVal("$.protectedData")).strVal("$.id")).isEqualTo("mock-id");
    }
}
