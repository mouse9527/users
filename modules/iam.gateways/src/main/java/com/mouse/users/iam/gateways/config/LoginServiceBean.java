package com.mouse.users.iam.gateways.config;

import com.mouse.users.iam.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginServiceBean {

    @Bean
    public LoginService loginService(PasswordMatcher passwordMatcher,
                                     UserRepository userRepository,
                                     TokenService tokenService,
                                     RoleService roleService) {
        return new LoginService(passwordMatcher, userRepository, tokenService, roleService);
    }
}
