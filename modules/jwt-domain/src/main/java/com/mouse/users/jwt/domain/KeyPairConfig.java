package com.mouse.users.jwt.domain;

import java.security.PrivateKey;
import java.security.PublicKey;

public interface KeyPairConfig {
    PublicKey getPublicKey();

    PrivateKey getPrivateKey();
}
