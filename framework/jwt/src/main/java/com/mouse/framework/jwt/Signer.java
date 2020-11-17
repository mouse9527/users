package com.mouse.framework.jwt;

public interface Signer {
    String sign(byte[] context);

    Header defaultHeader();
}
