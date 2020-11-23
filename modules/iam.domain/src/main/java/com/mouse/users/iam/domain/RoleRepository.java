package com.mouse.users.iam.domain;

public interface RoleRepository {
    String COLLECTION_NAME = "roles";

    Role load(String id);
}
