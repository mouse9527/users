package com.mouse.users.iam.domain;

public interface UserRepository {
    String COLLECTION_NAME = "users";

    void save(User user);
}
