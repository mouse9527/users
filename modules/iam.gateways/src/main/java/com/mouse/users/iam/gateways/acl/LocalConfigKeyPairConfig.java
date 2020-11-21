package com.mouse.users.iam.gateways.acl;

import com.mouse.framework.domain.core.Base64Util;
import com.mouse.framework.jwt.domain.KeyPairConfig;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Setter
@Component
@ConfigurationProperties(prefix = "application.iam.jwt")
public class LocalConfigKeyPairConfig implements KeyPairConfig, InitializingBean {
    private String publicKey;
    private String privateKey;

    private KeyPair keyPair;

    @Override
    public void afterPropertiesSet() throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(Base64Util.decode(publicKey));
        KeyFactory factory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = factory.generatePublic(spec);
        PrivateKey privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64Util.decode(this.privateKey)));
        this.keyPair = new KeyPair(publicKey, privateKey);
    }

    @Override
    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    @Override
    public PrivateKey getPrivateKey() {
        return keyPair.getPrivate();
    }
}
