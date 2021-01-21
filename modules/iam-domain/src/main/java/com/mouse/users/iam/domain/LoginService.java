package com.mouse.users.iam.domain;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Supplier;

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
        Supplier<UserNamePasswordErrorException> exceptionSupplier = () -> new UserNamePasswordErrorException("error.username-password-error");
        User user = optional.orElseThrow(exceptionSupplier);
        if (!user.matchPassword(password, passwordMatcher)) throw exceptionSupplier.get();
        return tokenService.allocate(user, roleService.loadAuthorities(user.getId()), action);
    }
}
