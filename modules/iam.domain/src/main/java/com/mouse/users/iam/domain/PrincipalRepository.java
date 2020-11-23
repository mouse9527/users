package com.mouse.users.iam.domain;

public interface PrincipalRepository {
    String COLLECTION_NAME = "principal";

    Principal load(String userId);
}
