package com.mouse.users.iam.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserTest {
    @Test
    void should_be_able_to_match_password() {
        PasswordMatcher passwordMatcher = mock(PasswordMatcher.class);
        given(passwordMatcher.match("password", "encoded-password")).willReturn(true);

        User user = new User("mock-user-id", "admin", "管理员", "encoded-password");

        assertThat(user.matchPassword("password", passwordMatcher)).isTrue();
    }
}