package com.mouse.users.iam.domain;

import java.util.Optional;

public interface UserRepository {
    String COLLECTION_NAME = "users";

    Optional<User> loadByUsername(String username);
}
