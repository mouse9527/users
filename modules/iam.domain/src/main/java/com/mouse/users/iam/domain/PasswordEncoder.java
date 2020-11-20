package com.mouse.users.iam.domain;

public interface PasswordEncoder {
    String encode(String rawPassword);
}
