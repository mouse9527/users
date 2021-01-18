package com.mouse.users.iam.domain;

import java.util.Optional;

public interface UserRepository {
    String COLLECTION_NAME = "users";

    void save(User user);

    Optional<User> loadByUsername(String username);
}
