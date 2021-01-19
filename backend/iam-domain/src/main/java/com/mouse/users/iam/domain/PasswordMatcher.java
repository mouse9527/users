package com.mouse.users.iam.domain;

public interface PasswordMatcher {

    Boolean matches(String rawPassword, String encodedPassword);
}
