package com.mouse.users.iam.domain;

import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthoritiesSetTest {
    @Test
    void should_be_able_to_merge_correctly() {
        AuthoritiesSet authoritiesSet = new AuthoritiesSet("authority-1", "authority-2");
        AuthoritiesSet authoritiesSet1 = new AuthoritiesSet("authority-1", "authority-3");

        AuthoritiesSet result = authoritiesSet.merge(authoritiesSet1);

        assertThat(authoritiesSet.getAuthorities()).isEqualTo(Set.of("authority-1", "authority-2"));
        assertThat(result.getAuthorities()).isEqualTo(Set.of("authority-1", "authority-2", "authority-3"));
        assertThat(authoritiesSet1.getAuthorities()).isEqualTo(Set.of("authority-1", "authority-3"));
    }
}
