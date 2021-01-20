package com.mouse.users.jwt.domain;

public interface Signer {
    String sign(String data);

    String encrypt(String data);

    Header defaultHeader();
}
