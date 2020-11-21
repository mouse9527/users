package com.mouse.users.iam.integration;

import com.jayway.jsonpath.JsonPath;
import com.mouse.framework.domain.core.Base64Util;
import com.mouse.framework.jwt.domain.Verifier;
import com.mouse.users.iam.domain.PasswordEncoder;
import com.mouse.users.iam.domain.User;
import com.mouse.users.iam.domain.UserRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllocationTokenTest {
    private @Resource TestRestTemplate testRestTemplate;
    private @Resource Verifier verifier;
    private @Resource UserRepository userRepository;
    private @Resource PasswordEncoder passwordEncoder;

    @Test
    void should_be_able_to_allocation_token() {
        User user = new User("admin", "管理员", passwordEncoder.encode("xxx"));
        userRepository.save(user);
        Map<String, String> body = new HashMap<>();
        body.put("username", "admin");
        body.put("password", "xxx");
        RequestEntity<?> entity = new RequestEntity<>(body, HttpMethod.POST, URI.create("/auth/tokens"));

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(entity, String.class);
        JsonResponse response = new JsonResponse(responseEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String accessToken = response.strValue("$.accessToken");
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken.split("\\.")).hasSize(3);
        String accessTokenPayload = Base64Util.decodeToString(accessToken.split("\\.")[1]);
        Instant iat = Instant.ofEpochSecond(JsonPath.compile("$.iat").<Integer>read(accessTokenPayload));
        assertThat(iat).isBefore(Instant.now());
        Instant exp = Instant.ofEpochSecond(JsonPath.compile("$.exp").<Integer>read(accessTokenPayload));
        assertThat(exp).isEqualTo(iat.plus(7, DAYS));
        assertThat(JsonPath.compile("$.name").<String>read(accessTokenPayload)).isEqualTo("管理员");
        assertThat(JsonPath.compile("$.jti").<String>read(accessTokenPayload)).hasSize(32);
        assertThat(JsonPath.compile("$.authorities[0]").<String>read(accessTokenPayload)).isEqualTo("view-users");
        assertThat(JsonPath.compile("$.scopes[0]").<String>read(accessTokenPayload)).isEqualTo("all");
        String protectedData = JsonPath.compile("$.protectedData").read(accessTokenPayload);
        assertThat(JsonPath.compile("$.userId").<String>read(verifier.decrypt(protectedData))).isEqualTo("mock-user-id");

        String refreshToken = response.strValue("$.refreshToken");
        assertThat(refreshToken).isNotEmpty();
        assertThat(refreshToken.split("\\.")).hasSize(3);
        String refreshTokenPayload = Base64Util.decodeToString(accessToken.split("\\.")[1]);
        Instant refreshTokenIat = Instant.ofEpochSecond(JsonPath.compile("$.iat").<Integer>read(refreshTokenPayload));
        assertThat(refreshTokenIat).isBefore(Instant.now());
        Instant refreshTokenExp = Instant.ofEpochSecond(JsonPath.compile("$.exp").<Integer>read(refreshTokenPayload));
        assertThat(refreshTokenExp).isEqualTo(refreshTokenIat.plus(30, DAYS));
        assertThat(JsonPath.compile("$.jti").<String>read(refreshTokenPayload)).hasSize(32);
        assertThat(JsonPath.compile("$.authorities[0]").<String>read(refreshTokenPayload)).isEqualTo("refresh-token");
        assertThat(JsonPath.compile("$.scopes[0]").<String>read(refreshTokenPayload)).isEqualTo("all");
        String refreshTokenProtectedData = JsonPath.compile("$.protectedData").read(refreshTokenPayload);
        assertThat(JsonPath.compile("$.userId").<String>read(verifier.decrypt(refreshTokenProtectedData))).isEqualTo("mock-user-id");
    }
}
