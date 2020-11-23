package com.mouse.users.iam.domain;

public interface PrincipalRepository {
    Principal load(String userId);
}
