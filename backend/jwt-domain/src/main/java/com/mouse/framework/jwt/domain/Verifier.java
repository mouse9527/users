package com.mouse.framework.jwt.domain;

public interface Verifier {
    boolean verify(String signature, String data);

    String decrypt(String encrypted);
}
