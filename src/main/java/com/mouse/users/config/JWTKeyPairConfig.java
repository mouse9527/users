package com.mouse.users.config;

import com.mouse.users.jwt.domain.FileKeyPairConfig;
import com.mouse.users.jwt.domain.KeyPairConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class JWTKeyPairConfig {
    @Value("${application.iam.jwt.private-key}")
    private String privateKey;
    @Value("${application.iam.jwt.public-key}")
    private String publicKey;

    @Bean
    public KeyPairConfig keyPairConfig() throws IOException {
        try (
                InputStream privateKey = ResourceUtils.getURL(this.privateKey).openStream();
                InputStream publicKey = ResourceUtils.getURL(this.publicKey).openStream()
        ) {
            return new FileKeyPairConfig(privateKey, publicKey);
        }
    }
}
