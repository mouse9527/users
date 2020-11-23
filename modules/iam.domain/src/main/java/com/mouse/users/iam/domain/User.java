package com.mouse.users.iam.domain;

public class User {
    private String password;
    private String id;
    private String name;

    public User() {
    }

    public User(String id, String username, String name, String password) {
        this.id = id;
        this.password = password;
        this.name = name;
    }

    public boolean matchPassword(String rawPassword, PasswordMatcher passwordMatcher) {
        return passwordMatcher.match(rawPassword, password);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
