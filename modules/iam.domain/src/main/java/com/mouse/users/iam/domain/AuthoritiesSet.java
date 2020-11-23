package com.mouse.users.iam.domain;

import com.google.common.collect.Sets;

import java.util.Set;

public class AuthoritiesSet {
    private Set<String> authorities;

    public AuthoritiesSet(String... authorities) {
        this.authorities = Sets.newHashSet(authorities);
    }

    public AuthoritiesSet(Set<String> authorities) {
        this.authorities = authorities;
    }

    public AuthoritiesSet merge(AuthoritiesSet authoritiesSet) {
        Set<String> current = Sets.newHashSet(authorities);
        current.addAll(authoritiesSet.getAuthorities());
        return new AuthoritiesSet(current);
    }

    public Set<String> getAuthorities() {
        return authorities;
    }
}
