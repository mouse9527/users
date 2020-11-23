package com.mouse.users.iam.gateways.config;

import com.mouse.framework.jwt.domain.Signer;
import com.mouse.users.iam.domain.RefreshTokenRepository;
import com.mouse.users.iam.domain.TokenService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenServiceBean {

    @Bean
    public TokenService tokenService(Signer signer, RefreshTokenRepository refreshTokenRepository) {
        return new TokenService(signer, refreshTokenRepository);
    }
}
