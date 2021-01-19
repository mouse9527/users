package com.mouse.users.iam.config;

import com.mouse.framework.jwt.domain.FileKeyPairConfig;
import com.mouse.framework.jwt.domain.KeyPairConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.nio.file.Path;

@Configuration
public class JWTKeyPairConfig {
    @Value("${application.iam.jwt.private-key}")
    private String privateKey;
    @Value("${application.iam.jwt.public-key}")
    private String publicKey;

    @Bean
    public KeyPairConfig keyPairConfig() throws FileNotFoundException, URISyntaxException {
        return new FileKeyPairConfig(Path.of(ResourceUtils.getURL(privateKey).toURI()), Path.of(ResourceUtils.getURL(publicKey).toURI()));
    }
}
