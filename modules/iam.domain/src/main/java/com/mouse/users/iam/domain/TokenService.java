package com.mouse.users.iam.domain;

import com.mouse.framework.common.domain.TokenSigner;
import com.mouse.framework.users.core.Payload;

import java.time.Instant;

public class TokenService {
    private final TokenSigner tokenSigner;

    public TokenService(TokenSigner tokenSigner) {
        this.tokenSigner = tokenSigner;
    }

    public String allocate() {
        Payload<String> payload = Payload.<String>builder()
                .iat(Instant.now())
                .typ("visitor")
                .build();
        return tokenSigner.sign(payload);
    }
}
