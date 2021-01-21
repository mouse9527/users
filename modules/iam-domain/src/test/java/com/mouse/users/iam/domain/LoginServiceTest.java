package com.mouse.users.iam.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class LoginServiceTest {
    private static final AuthoritiesSet AUTHORITIES_SET = new AuthoritiesSet("mock-authority-id");
    private static final String MOCK_USER_ID = "mock-user-id";
    private static final Instant ACTION = Instant.now();
    private LoginService loginService;
    private TokenResult expectedTokenResult;

    @BeforeEach
    void setUp() {
        expectedTokenResult = new TokenResult("mock-access-token", "mock-refresh-token");
        PasswordMatcher passwordMatcher = mock(PasswordMatcher.class);
        given(passwordMatcher.matches("123456", "mock-encoded-password")).willReturn(true);
        UserRepository repository = mock(UserRepository.class);
        User user = new User(MOCK_USER_ID, "admin", "name", "mock-encoded-password");
        given(repository.loadByUsername("admin")).willReturn(Optional.of(user));
        RoleService roleService = mock(RoleService.class);
        given(roleService.loadAuthorities(MOCK_USER_ID)).willReturn(AUTHORITIES_SET);
        TokenService tokenService = mock(TokenService.class);
        given(tokenService.allocate(user, AUTHORITIES_SET, ACTION)).willReturn(expectedTokenResult);
        loginService = new LoginService(passwordMatcher, repository, tokenService, roleService);
    }

    @Test
    void should_be_able_to_allocate_token_with_username_and_password() {
        TokenResult tokenResult = loginService.allocate("admin", "123456", ACTION);

        assertThat(tokenResult).isEqualTo(expectedTokenResult);
    }

    @Test
    void should_be_able_to_raise_exception_when_user_not_found() {
        Throwable throwable = catchThrowable(() -> loginService.allocate("unknown-user", "xxx", ACTION));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(UserNamePasswordErrorException.class);
        assertThat(throwable).hasMessage("error.username-password-error");
    }

    @Test
    void should_be_able_to_raise_exception_when_password_not_match() {
        Throwable throwable = catchThrowable(() -> loginService.allocate("admin", "unknown-password", ACTION));

        assertThat(throwable).isNotNull();
        assertThat(throwable).isInstanceOf(UserNamePasswordErrorException.class);
        assertThat(throwable).hasMessage("error.username-password-error");
    }
}
