package com.mouse.framework.jwt;

public interface TokenSigner {
    String sign(Payload<?> payload);
}
