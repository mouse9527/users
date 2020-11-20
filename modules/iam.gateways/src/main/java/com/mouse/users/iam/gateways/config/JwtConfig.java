package com.mouse.users.iam.gateways.config;

import com.mouse.framework.jwt.domain.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JwtConfig {

    @Bean
    public Verifier verifier(KeyPairConfig keyPairConfig) {
        return new RSAVerifier(keyPairConfig.getPublicKey());
    }

    @Bean
    public Signer signer(KeyPairConfig keyPairConfig) {
        return new RSASigner(keyPairConfig.getPrivateKey());
    }
}
