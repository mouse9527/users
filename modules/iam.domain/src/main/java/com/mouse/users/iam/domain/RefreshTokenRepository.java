package com.mouse.users.iam.domain;

public interface RefreshTokenRepository {
    String COLLECTION_NAME = "refresh_tokens";

    void save(RefreshToken refreshToken);
}
