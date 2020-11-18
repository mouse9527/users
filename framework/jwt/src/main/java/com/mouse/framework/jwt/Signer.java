package com.mouse.framework.jwt;

public interface Signer {
    String sign(Payload<?> payload);
}
