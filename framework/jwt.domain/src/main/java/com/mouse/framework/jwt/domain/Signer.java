package com.mouse.framework.jwt.domain;

// TODO: rename it
public interface Signer {
    String sign(String data);

    String encrypt(String data);
}
