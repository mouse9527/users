package com.mouse.users.iam.domain;

import com.mouse.users.jwt.domain.JwtBuilder;
import com.mouse.users.jwt.domain.Signer;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

public class TokenService {
    public static final int ACCESS_TOKEN_EXP_DAYS = 7;
    public static final int REFRESH_TOKEN_EXP_DAYS = 30;
    private final Signer signer;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenService(Signer signer, RefreshTokenRepository refreshTokenRepository) {
        this.signer = signer;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public TokenResult allocate(User user, AuthoritiesSet authoritiesSet, Instant iat) {
        String accessTokenId = nextId();
        Instant accessTokenExp = iat.plus(ACCESS_TOKEN_EXP_DAYS, DAYS);
        String accessToken = new JwtBuilder()
                .jti(accessTokenId)
                .iat(iat.getEpochSecond())
                .exp(accessTokenExp.getEpochSecond())
                .authorities(authoritiesSet.getAuthorities())
                .name(user.getName())
                .protectedData(Collections.singletonMap("userId", user.getId()))
                .sign(signer);

        String refreshTokenId = nextId();
        Instant refreshTokenExp = iat.plus(REFRESH_TOKEN_EXP_DAYS, DAYS);
        String refreshJWT = new JwtBuilder()
                .jti(refreshTokenId)
                .iat(iat.getEpochSecond())
                .exp(refreshTokenExp.getEpochSecond())
                .authorities(Collections.singletonList("refresh-token"))
                .sign(signer);

        RefreshToken refreshToken = new RefreshToken(refreshTokenId, accessTokenId, iat, accessTokenExp, refreshTokenExp);
        refreshTokenRepository.save(refreshToken);
        return new TokenResult(accessToken, refreshJWT);
    }

    private String nextId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
