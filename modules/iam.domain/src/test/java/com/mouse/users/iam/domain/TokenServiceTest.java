package com.mouse.users.iam.domain;

import com.jayway.jsonpath.JsonPath;
import com.mouse.framework.common.domain.Base64Util;
import com.mouse.framework.common.domain.RSATokenSigner;
import com.mouse.framework.common.domain.TokenSigner;
import org.junit.jupiter.api.Test;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenServiceTest {
    @Test
    void should_be_able_to_allocate_token() throws NoSuchAlgorithmException {
        KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(1024);
        KeyPair keyPair = generator.generateKeyPair();
        TokenSigner tokenSigner = new RSATokenSigner(keyPair.getPrivate());

        TokenService tokenService = new TokenService(tokenSigner);

        String jwt = tokenService.allocate();

        String[] split = jwt.split("\\.");
        assertThat(split).hasSize(3);
        assertThat(JsonPath.compile("$.typ").<String>read(Base64Util.decodeToString(split[0]))).isEqualTo("RSA1024");
        assertThat(JsonPath.compile("$.alg").<String>read(Base64Util.decodeToString(split[0]))).isEqualTo("JWT");
        assertThat(JsonPath.compile("$.typ").<String>read(Base64Util.decodeToString(split[1]))).isEqualTo("visitor");
    }
}
