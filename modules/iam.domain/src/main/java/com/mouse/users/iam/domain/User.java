package com.mouse.users.iam.domain;

public class User {
    private String password;

    public User() {
    }

    public User(String username, String name, String password) {
        this.password = password;
    }

    public boolean matchPassword(String rawPassword, PasswordMatcher passwordMatcher) {
        return passwordMatcher.match(rawPassword, password);
    }
}
