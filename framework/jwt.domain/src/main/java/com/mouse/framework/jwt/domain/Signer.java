package com.mouse.framework.jwt.domain;

public interface Signer {
    String sign(String data);

    String encrypt(String data);
}
