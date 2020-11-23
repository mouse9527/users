package com.mouse.users.iam.gateways.config;

import com.mouse.users.iam.domain.PrincipalRepository;
import com.mouse.users.iam.domain.RoleRepository;
import com.mouse.users.iam.domain.RoleService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RoleServiceBean {

    @Bean
    public RoleService roleService(PrincipalRepository principalRepository, RoleRepository roleRepsoritry) {
        return new RoleService(principalRepository, roleRepsoritry);
    }
}
