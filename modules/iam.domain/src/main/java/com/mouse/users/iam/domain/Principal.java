package com.mouse.users.iam.domain;

import java.util.Set;

public class Principal {
    private String id;
    private Set<String> roleIds;

    public Principal(String id, Set<String> roleIds) {
        this.id = id;
        this.roleIds = roleIds;
    }

    public Set<String> getRoleIds() {
        return roleIds;
    }
}
