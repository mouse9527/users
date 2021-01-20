package com.mouse.users.iam.domain;

public class RoleService {
    private final PrincipalRepository principalRepository;
    private final RoleRepository roleRepository;

    public RoleService(PrincipalRepository principalRepository, RoleRepository roleRepository) {
        this.principalRepository = principalRepository;
        this.roleRepository = roleRepository;
    }

    public AuthoritiesSet loadAuthorities(String userId) {
        Principal principal = principalRepository.load(userId);
        return principal.getRoleIds().stream()
                .map(roleRepository::load)
                .map(Role::getAuthorities)
                .reduce(new AuthoritiesSet(), AuthoritiesSet::merge);
    }
}
