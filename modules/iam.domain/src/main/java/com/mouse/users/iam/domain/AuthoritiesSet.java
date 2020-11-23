package com.mouse.users.iam.domain;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashSet;
import java.util.Set;

public class AuthoritiesSet {
    private Set<String> authorities;

    protected AuthoritiesSet() {
    }

    public AuthoritiesSet(String... authorities) {
        this.authorities = Sets.newHashSet(authorities);
    }

    public AuthoritiesSet(Set<String> authorities) {
        this.authorities = authorities;
    }

    public AuthoritiesSet merge(AuthoritiesSet authoritiesSet) {
        Set<String> current = Sets.newHashSet(getAuthorities());
        current.addAll(authoritiesSet.getAuthorities());
        return new AuthoritiesSet(current);
    }

    public Set<String> getAuthorities() {
        return ObjectUtils.defaultIfNull(authorities, new HashSet<>());
    }
}
