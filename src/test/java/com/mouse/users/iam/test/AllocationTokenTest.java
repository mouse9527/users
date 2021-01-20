package com.mouse.users.iam.test;

import com.mouse.framework.test.EnableEmbeddedMongoDB;
import com.mouse.framework.test.JsonObject;
import com.mouse.users.iam.domain.*;
import com.mouse.users.jwt.domain.Verifier;
import com.mouse.uses.domain.core.Base64Util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import javax.annotation.Resource;
import java.net.URI;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@EnableEmbeddedMongoDB
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AllocationTokenTest {
    private static final String MOCK_USER_ID = "mock-user-id";
    private static final String MOCK_ROLE_ID = "mock-role-id";
    private static final String MOCK_AUTHORITIES = "mock-authority";
    @Resource
    private TestRestTemplate testRestTemplate;
    @Resource
    private Verifier verifier;
    @Resource
    private UserRepository userRepository;
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
        Map<String, String> body = new HashMap<>();
        body.put("username", "admin");
        body.put("password", "xxx");
        RequestEntity<?> entity = new RequestEntity<>(body, HttpMethod.POST, URI.create("/auth/tokens"));
        Instant start = Instant.ofEpochSecond(Instant.now().getEpochSecond());

        ResponseEntity<String> responseEntity = testRestTemplate.exchange(entity, String.class);
        JsonResponse response = new JsonResponse(responseEntity);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        String accessToken = response.strValue("$.accessToken");
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken.split("\\.")).hasSize(3);
        JsonObject accessTokenPayload = new JsonObject(Base64Util.decodeToString(accessToken.split("\\.")[1]));
        Instant iat = Instant.ofEpochSecond(accessTokenPayload.intVal("$.iat"));
        assertThat(iat).isBefore(Instant.now());
        Instant exp = Instant.ofEpochSecond(accessTokenPayload.intVal("$.exp"));
        assertThat(exp).isEqualTo(iat.plus(7, DAYS));
        assertThat(accessTokenPayload.strVal("$.name")).isEqualTo("管理员");
        assertThat(accessTokenPayload.strVal("$.jti")).hasSize(32);
        assertThat(accessTokenPayload.strVal("$.authorities[0]")).isEqualTo(MOCK_AUTHORITIES);
        String protectedData = accessTokenPayload.strVal("$.protectedData");
        assertThat(new JsonObject(verifier.decrypt(protectedData)).strVal("$.userId")).isEqualTo(MOCK_USER_ID);

        String refreshToken = response.strValue("$.refreshToken");
        assertThat(refreshToken).isNotEmpty();
        assertThat(refreshToken.split("\\.")).hasSize(3);
        JsonObject refreshJwtPayload = new JsonObject(Base64Util.decodeToString(refreshToken.split("\\.")[1]));
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
        userRepository.save(user);
        Principal principal = new Principal(MOCK_USER_ID, Set.of(MOCK_ROLE_ID));
        mongoTemplate.save(principal, PrincipalRepository.COLLECTION_NAME);
        Role role = new Role(MOCK_ROLE_ID, "mock-role", new AuthoritiesSet(MOCK_AUTHORITIES));
        mongoTemplate.save(role, RoleRepository.COLLECTION_NAME);
    }
}
