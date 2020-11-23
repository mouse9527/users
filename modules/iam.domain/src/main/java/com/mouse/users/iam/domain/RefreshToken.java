package com.mouse.users.iam.domain;

import lombok.EqualsAndHashCode;

import java.time.Instant;

@EqualsAndHashCode
public class RefreshToken {
    private String id;
    private String activeAccessTokenId;
    private Instant iat;
    private Instant accessTokenExp;
    private Instant refreshTokenExp;

    public RefreshToken(String id, String activeAccessTokenId, Instant iat, Instant accessTokenExp, Instant refreshTokenExp) {
        this.id = id;
        this.activeAccessTokenId = activeAccessTokenId;
        this.iat = iat;
        this.accessTokenExp = accessTokenExp;
        this.refreshTokenExp = refreshTokenExp;
    }
}
