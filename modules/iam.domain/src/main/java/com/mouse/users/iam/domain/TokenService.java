package com.mouse.users.iam.domain;

import com.mouse.framework.jwt.domain.JwtBuilder;
import com.mouse.framework.jwt.domain.Signer;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.DAYS;

public class TokenService {
    public static final int ACCESS_TOKEN_EXP_DAYS = 7;
    public static final int REFRESH_TOKEN_EXP_DAYS = 30;
    private final Signer signer;

    public TokenService(Signer signer) {
        this.signer = signer;
    }

    public TokenResult allocate(String userId, List<String> authorities, Instant iat) {
        String accessToken = new JwtBuilder()
                .jti(UUID.randomUUID().toString().replaceAll("-", ""))
                .iat(iat.getEpochSecond())
                .exp(iat.plus(ACCESS_TOKEN_EXP_DAYS, DAYS).getEpochSecond())
                .authorities(authorities)
                .protectedData(Collections.singletonMap("userId", userId))
                .sign(signer);
        String refreshToken = new JwtBuilder()
                .jti(UUID.randomUUID().toString().replaceAll("-", ""))
                .iat(iat.getEpochSecond())
                .exp(iat.plus(REFRESH_TOKEN_EXP_DAYS, DAYS).getEpochSecond())
                .authorities(Collections.singletonList("refresh-token"))
                .sign(signer);
        return new TokenResult(accessToken, refreshToken);
    }
}
