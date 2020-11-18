package com.mouse.framework.jwt;

public interface Signer {
    String sign(byte[] context);

    String sign(Payload<?> payload);
}
