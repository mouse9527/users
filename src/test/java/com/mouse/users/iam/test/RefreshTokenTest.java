package com.mouse.users.iam.test;

import com.mouse.framework.test.TestClient;
import com.mouse.framework.test.TestJsonObject;
import com.mouse.users.iam.domain.*;
import com.mouse.users.jwt.domain.Verifier;
import com.mouse.uses.domain.core.Base64Util;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.*;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
class RefreshTokenTest {
    public static final String ADMIN = "admin";
    public static final String XXX = "xxx";
    private static final String MOCK_USER_ID = "mock-user-id";
    private static final String MOCK_ROLE_ID = "mock-role-id";
    private static final String MOCK_AUTHORITIES = "mock-authority";
    @Resource
    private TestClient testClient;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private Verifier verifier;

    @Test
    @Disabled
    void should_be_able_to_refresh_token_with_signed_refresh_token() {
        Map<String, Object> body = Collections.singletonMap("refreshToken", getRefreshToken());

        ResponseEntity<TestJsonObject> response = testClient.post("/auth/tokens/actions/refresh", body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().strVal("$.accessToken")).isNotNull();
        String accessToken = response.getBody().strVal("$.accessToken");
        assertThat(accessToken.split("\\.")).hasSize(3);
        TestJsonObject accessTokenPayload = new TestJsonObject(Base64Util.decodeToString(accessToken.split("\\.")[1]));
        Instant iat = Instant.ofEpochSecond(accessTokenPayload.intVal("$.iat"));
        assertThat(iat).isBefore(Instant.now());
        Instant exp = Instant.ofEpochSecond(accessTokenPayload.intVal("$.exp"));
        assertThat(exp).isEqualTo(iat.plus(7, DAYS));
        assertThat(accessTokenPayload.strVal("$.name")).isEqualTo("管理员");
        assertThat(accessTokenPayload.strVal("$.jti")).hasSize(32);
        assertThat(accessTokenPayload.strVal("$.authorities[0]")).isEqualTo(MOCK_AUTHORITIES);
        String protectedData = accessTokenPayload.strVal("$.protectedData");
        assertThat(new TestJsonObject(verifier.decrypt(protectedData)).strVal("$.userId")).isEqualTo(MOCK_USER_ID);

    }

    private String getRefreshToken() {
        mockData();
        Map<String, Object> loginBody = new HashMap<>();
        loginBody.put("username", ADMIN);
        loginBody.put("password", XXX);
        ResponseEntity<TestJsonObject> loginResponse = testClient.post("/auth/tokens", loginBody);
        return Objects.requireNonNull(loginResponse.getBody()).strVal("$.refreshToken");
    }

    private void mockData() {
        User user = new User(MOCK_USER_ID, ADMIN, "管理员", passwordEncoder.encode(XXX));
        mongoTemplate.save(user, UserRepository.COLLECTION_NAME);
        Principal principal = new Principal(MOCK_USER_ID, Set.of(MOCK_ROLE_ID));
        mongoTemplate.save(principal, PrincipalRepository.COLLECTION_NAME);
        Role role = new Role(MOCK_ROLE_ID, "mock-role", new AuthoritiesSet(MOCK_AUTHORITIES));
        mongoTemplate.save(role, RoleRepository.COLLECTION_NAME);
    }
}
