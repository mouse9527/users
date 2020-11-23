package com.mouse.users.iam.domain;

public class TokenResult {
    private final String accessToken;
    private final String refreshToken;

    public TokenResult(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
