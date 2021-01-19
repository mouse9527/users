package com.mouse.users.iam.domain;

import com.mouse.framework.jwt.domain.Header;
import com.mouse.framework.jwt.domain.Signer;
import com.mouse.framework.test.JsonObject;
import com.mouse.uses.domain.core.Base64Util;
import org.junit.jupiter.api.Test;
import org.mockito.internal.verification.Times;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.DAYS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

class TokenServiceTest {
    @Test
    void should_be_able_to_allocate_access_token() {
        Signer signer = mockSigner();
        RefreshTokenRepository refreshTokenRepository = mock(RefreshTokenRepository.class);
        TokenService tokenService = new TokenService(signer, refreshTokenRepository);
        Instant now = Instant.now();

        User user = new User("mock-user-id", "admin", "管理员", "xxx");

        TokenResult tokenResult = tokenService.allocate(user, new AuthoritiesSet("mock-authority"), now);

        String accessToken = tokenResult.getAccessToken();
        assertThat(accessToken).isNotEmpty();
        assertThat(accessToken.split("\\.")).hasSize(3);
        JsonObject accessTokenPayload = new JsonObject(Base64Util.decodeToString(accessToken.split("\\.")[1]));
        String accessTokenId = accessTokenPayload.strVal("$.jti");
        assertThat(accessTokenId).hasSize(32);
        assertThat(accessTokenPayload.intVal("$.iat")).isEqualTo(now.getEpochSecond());
        Instant accessTokenExp = now.plus(7, DAYS);
        assertThat(accessTokenPayload.intVal("$.exp")).isEqualTo(accessTokenExp.getEpochSecond());
        assertThat(accessTokenPayload.strVal("$.authorities[0]")).isEqualTo("mock-authority");
        assertThat(accessTokenPayload.has("$.authorities[1]")).isFalse();
        assertThat(accessTokenPayload.strVal("$.protectedData")).isEqualTo("mock-encrypted");
        assertThat(accessTokenPayload.strVal("$.name")).isEqualTo("管理员");
        String refreshToken = tokenResult.getRefreshToken();
        assertThat(refreshToken).isNotEmpty();
        JsonObject refreshTokenPayload = new JsonObject(Base64Util.decodeToString(refreshToken.split("\\.")[1]));
        String refreshTokenId = refreshTokenPayload.strVal("$.jti");
        assertThat(refreshTokenId).hasSize(32);
        assertThat(refreshTokenPayload.intVal("$.iat")).isEqualTo(now.getEpochSecond());
        Instant refreshTokenExp = now.plus(30, DAYS);
        assertThat(refreshTokenPayload.intVal("$.exp")).isEqualTo(refreshTokenExp.getEpochSecond());
        assertThat(refreshTokenPayload.strVal("$.authorities[0]")).isEqualTo("refresh-token");
        assertThat(refreshTokenPayload.has("$.protectedData")).isFalse();
        RefreshToken expected = new RefreshToken(refreshTokenId, accessTokenId, now, accessTokenExp, refreshTokenExp);
        then(refreshTokenRepository).should(new Times(1)).save(expected);
    }

    private Signer mockSigner() {
        Signer signer = mock(Signer.class);
        given(signer.encrypt(anyString())).willReturn("mock-encrypted");
        given(signer.defaultHeader()).willReturn(new Header("RSA1024"));
        given(signer.sign(anyString())).willReturn("mock-signature");
        return signer;
    }
}
