package com.mouse.framework.common.domain;


import com.mouse.framework.users.core.Payload;

public interface TokenSigner {
    String sign(Payload<?> payload);
}
