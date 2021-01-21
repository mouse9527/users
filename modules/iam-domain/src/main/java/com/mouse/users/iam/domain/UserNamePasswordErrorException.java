package com.mouse.users.iam.domain;

public class UserNamePasswordErrorException extends RuntimeException {

    public UserNamePasswordErrorException(String message) {
        super(message);
    }
}
