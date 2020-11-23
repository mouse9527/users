package com.mouse.users.iam.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class RoleServiceTest {
    private static final String MOCK_USER_ID = "mock-user-id";
    private static final String MOCK_ROLE_ID_1 = "mock-role-id-1";
    private static final String MOCK_ROLE_ID_2 = "mock-role-id-2";
    private RoleService roleService;
    private RoleRepository roleRepository;
    private PrincipalRepository principalRepository;

    @BeforeEach
    void setUp() {
        principalRepository = mock(PrincipalRepository.class);
        given(principalRepository.load(MOCK_USER_ID)).willReturn(new Principal(MOCK_USER_ID, Set.of(MOCK_ROLE_ID_1, MOCK_ROLE_ID_2)));
        roleRepository = mock(RoleRepository.class);
        given(roleRepository.load(MOCK_ROLE_ID_1)).willReturn(new Role(MOCK_ROLE_ID_1, "role1", new AuthoritiesSet("authority-1", "authority-2")));
        given(roleRepository.load(MOCK_ROLE_ID_2)).willReturn(new Role(MOCK_ROLE_ID_2, "role2", new AuthoritiesSet("authority-1", "authority-3")));
        roleService = new RoleService(principalRepository, roleRepository);
    }

    @Test
    void should_be_able_to_load_all_authorities_set_with_user_id() {
        AuthoritiesSet authoritiesSet = roleService.loadAuthorities(MOCK_USER_ID);

        assertThat(authoritiesSet.getAuthorities()).isEqualTo(Set.of("authority-1", "authority-2", "authority-3"));
    }

    @Test
    void should_be_able_to_load_empty_without_any_authorities() {
        given(principalRepository.load(MOCK_USER_ID)).willReturn(new Principal(MOCK_USER_ID, Set.of(MOCK_ROLE_ID_1, MOCK_ROLE_ID_2)));
        given(roleRepository.load(MOCK_ROLE_ID_1)).willReturn(new Role(MOCK_ROLE_ID_1, "role1", new AuthoritiesSet()));
        given(roleRepository.load(MOCK_ROLE_ID_2)).willReturn(new Role(MOCK_ROLE_ID_2, "role2", new AuthoritiesSet()));

        AuthoritiesSet authoritiesSet = roleService.loadAuthorities(MOCK_USER_ID);

        assertThat(authoritiesSet.getAuthorities()).isEmpty();
    }

    @Test
    void should_be_able_to_load_empty_without_roles() {
        given(principalRepository.load(MOCK_USER_ID)).willReturn(new Principal(MOCK_USER_ID, Collections.emptySet()));

        AuthoritiesSet authoritiesSet = roleService.loadAuthorities(MOCK_USER_ID);

        assertThat(authoritiesSet.getAuthorities()).isEmpty();
    }
}
