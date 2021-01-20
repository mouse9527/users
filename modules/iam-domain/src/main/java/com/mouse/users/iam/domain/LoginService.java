package com.mouse.users.iam.domain;

import java.time.Instant;
import java.util.Optional;

public class LoginService {
    private final PasswordMatcher passwordMatcher;
    private final UserRepository repository;
    private final TokenService tokenService;
    private final RoleService roleService;

    public LoginService(PasswordMatcher passwordMatcher,
                        UserRepository repository,
                        TokenService tokenService,
                        RoleService roleService) {
        this.passwordMatcher = passwordMatcher;
        this.repository = repository;
        this.tokenService = tokenService;
        this.roleService = roleService;
    }

    public TokenResult allocate(String username, String password, Instant action) {
        Optional<User> optional = repository.loadByUsername(username);
        User user = optional.get();
        if (user.matchPassword(password, passwordMatcher)) {
            return tokenService.allocate(user, roleService.loadAuthorities(user.getId()), action);
        }

        return null;
    }
}
