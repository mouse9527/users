package com.mouse.users.iam.domain;

public class User {
    private String id;
    private String name;
    private String username;
    private String password;

    public User() {
    }

    public User(String id, String username, String name, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
    }

    public boolean matchPassword(String rawPassword, PasswordMatcher passwordMatcher) {
        return passwordMatcher.matches(rawPassword, password);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
