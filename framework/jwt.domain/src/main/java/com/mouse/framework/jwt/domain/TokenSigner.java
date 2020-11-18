package com.mouse.framework.jwt.domain;

public interface TokenSigner {
    String sign(Payload payload);
}
