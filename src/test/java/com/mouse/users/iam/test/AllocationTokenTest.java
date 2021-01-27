package com.mouse.users.iam.test;

import com.mouse.framework.test.EnableEmbeddedMongoDB;
import com.mouse.framework.test.TestClient;
import com.mouse.framework.test.TestJsonObject;
import com.mouse.users.iam.domain.*;
import com.mouse.users.jwt.domain.Verifier;
import com.mouse.uses.domain.core.Base64Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.annotation.Resource;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

@IntegrationTest
@EnableEmbeddedMongoDB
public class AllocationTokenTest {
    private static final String MOCK_USER_ID = "mock-user-id";
    private static final String MOCK_ROLE_ID = "mock-role-id";
    private static final String MOCK_AUTHORITIES = "mock-authority";
    @Resource
    private TestClient testClient;
    @Resource
    private Verifier verifier;
    @Resource
    private PasswordEncoder passwordEncoder;
    @Resource
    private MongoTemplate mongoTemplate;

    @AfterEach
    void tearDown() {
        mongoTemplate.dropCollection(UserRepository.COLLECTION_NAME);
        mongoTemplate.dropCollection(PrincipalRepository.COLLECTION_NAME);
        mongoTemplate.dropCollection(RoleRepository.COLLECTION_NAME);
    }

    @Test
    void should_be_able_to_allocation_token() {
        mockData();
        Map<String, Object> body = new HashMap<>();
        body.put("username", "admin");
        body.put("password", "xxx");
        Instant start = Instant.ofEpochSecond(Instant.now().getEpochSecond());

        ResponseEntity<TestJsonObject> response = testClient.post("/auth/tokens", body);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        TestJsonObject responseBody = response.getBody();
        assertThat(responseBody).isNotNull();
        String accessToken = responseBody.strVal("$.accessToken");
        assertThat(accessToken).isNotEmpty();
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

        String refreshToken = responseBody.strVal("$.refreshToken");
        assertThat(refreshToken).isNotEmpty();
        assertThat(refreshToken.split("\\.")).hasSize(3);
        TestJsonObject refreshJwtPayload = new TestJsonObject(Base64Util.decodeToString(refreshToken.split("\\.")[1]));
        Instant refreshTokenIat = Instant.ofEpochSecond(refreshJwtPayload.intVal("$.iat"));
        assertThat(refreshTokenIat).isBetween(start, Instant.now());
        Instant refreshTokenExp = Instant.ofEpochSecond(refreshJwtPayload.intVal("$.exp"));
        assertThat(refreshTokenExp).isEqualTo(refreshTokenIat.plus(30, DAYS));
        assertThat(refreshJwtPayload.strVal("$.jti")).hasSize(32);
        assertThat(refreshJwtPayload.strVal("$.authorities[0]")).isEqualTo("refresh-token");
        assertThat(refreshJwtPayload.has("$.protectedData")).isFalse();
    }

    private void mockData() {
        User user = new User(MOCK_USER_ID, "admin", "管理员", passwordEncoder.encode("xxx"));
        mongoTemplate.save(user, UserRepository.COLLECTION_NAME);
        Principal principal = new Principal(MOCK_USER_ID, Set.of(MOCK_ROLE_ID));
        mongoTemplate.save(principal, PrincipalRepository.COLLECTION_NAME);
        Role role = new Role(MOCK_ROLE_ID, "mock-role", new AuthoritiesSet(MOCK_AUTHORITIES));
        mongoTemplate.save(role, RoleRepository.COLLECTION_NAME);
    }
}
