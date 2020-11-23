package com.mouse.users.iam.domain;

import com.mouse.framework.domain.core.Base64Util;
import com.mouse.framework.jwt.domain.Header;
import com.mouse.framework.jwt.domain.Signer;
import com.mouse.framework.test.JsonObject;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class TokenServiceTest {
    @Test
    void should_be_able_to_allocate_access_token() {
        Signer signer = mock(Signer.class);
        given(signer.encrypt(anyString())).willReturn("mock-encrypted");
        given(signer.defaultHeader()).willReturn(new Header("RSA1024"));
        given(signer.sign(anyString())).willReturn("mock-signature");
        TokenService tokenService = new TokenService(signer);
        Instant now = Instant.now();

        TokenResult tokenResult = tokenService.allocate("user-id", Collections.singletonList("mock-authority"), now);

        String accessToken = tokenResult.getAccessToken();
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken.split("\\.")).hasSize(3);
        JsonObject accessTokenPayload = new JsonObject(Base64Util.decodeToString(accessToken.split("\\.")[1]));
        assertThat(accessTokenPayload.strVal("$.jti")).hasSize(32);
        assertThat(accessTokenPayload.intVal("$.iat")).isEqualTo(now.getEpochSecond());
        assertThat(accessTokenPayload.intVal("$.exp")).isEqualTo(now.plus(7, DAYS).getEpochSecond());
        assertThat(accessTokenPayload.strVal("$.authorities[0]")).isEqualTo("mock-authority");
        assertThat(accessTokenPayload.has("$.authorities[1]")).isFalse();
        assertThat(accessTokenPayload.strVal("$.protectedData")).isEqualTo("mock-encrypted");
        String refreshToken = tokenResult.getRefreshToken();
        assertThat(refreshToken).isNotEmpty();
        JsonObject refreshTokenPayload = new JsonObject(Base64Util.decodeToString(refreshToken.split("\\.")[1]));
        assertThat(refreshTokenPayload.strVal("$.jti")).hasSize(32);
        assertThat(refreshTokenPayload.intVal("$.iat")).isEqualTo(now.getEpochSecond());
        assertThat(refreshTokenPayload.intVal("$.exp")).isEqualTo(now.plus(30, DAYS).getEpochSecond());
        assertThat(refreshTokenPayload.strVal("$.authorities[0]")).isEqualTo("refresh-token");
        assertThat(refreshTokenPayload.has("$.protectedData")).isFalse();
    }
}
