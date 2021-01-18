package com.mouse.users.iam.application;

import com.mouse.users.iam.domain.LoginService;
import com.mouse.users.iam.domain.TokenResult;
import org.springframework.stereotype.Component;

import java.time.Instant;

@Component
public class LoginWithUsernamePasswdUseCase {
    private final LoginService loginService;

    public LoginWithUsernamePasswdUseCase(LoginService loginService) {
        this.loginService = loginService;
    }

    public TokenResult login(String username, String password) {
        return loginService.allocate(username, password, Instant.now());
    }
}
