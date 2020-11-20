package com.mouse.users.iam.domain;

public interface PasswordMatcher {

    Boolean match(String rawPassword, String encodedPassword);
}
